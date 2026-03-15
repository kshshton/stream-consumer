package com.example.sensorservice.topology

import com.example.sensorservice.model.SensorEvent
import com.example.sensorservice.serde.SensorEventSerde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Produced

class GenericSensorTopology(
    private val inputTopic: String = "iot.sensors.raw",
    private val outputTopic: String = "iot.sensors.processed",
    private val dlqTopic: String = "iot.sensors.dlq",
    private val validRange: ClosedRange<Double>,
    private val expectedRange: ClosedRange<Double>
) {

    fun build(): Topology {
        val builder = StreamsBuilder()
        val serde = SensorEventSerde.serde()

        val source = builder.stream(inputTopic, Consumed.with(Serdes.String(), serde))
        val valid = source.filter { _, e -> isValid(e, validRange) }
        val invalid = source.filterNot { _, e -> isValid(e, validRange) }

        valid
            .mapValues { e -> normalize(e) }
            .to(outputTopic, Produced.with(Serdes.String(), serde))

        invalid.to(dlqTopic, Produced.with(Serdes.String(), serde))

        return builder.build()
    }

    private fun isValid(event: SensorEvent, validRange: ClosedRange<Double>): Boolean {
        return event.sensorId.isNotBlank() && event.value in validRange
    }

    private fun normalize(event: SensorEvent): SensorEvent {
        val tags = if (event.value !in expectedRange) event.tags + "ANOMALY" else event.tags
        return event.copy(value = event.value, unit = event.unit, tags = tags)
    }
}
