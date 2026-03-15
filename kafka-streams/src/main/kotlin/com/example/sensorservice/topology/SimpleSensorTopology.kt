package com.example.sensorservice.topology

import com.example.sensorservice.model.SensorEvent
import com.example.sensorservice.serde.SensorEventSerde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Produced

object SimpleSensorTopology {
    const val INPUT_TOPIC = "iot.sensors.raw"
    const val OUTPUT_TOPIC = "iot.sensors.processed"
    const val DLQ_TOPIC = "iot.sensors.dlq"
    private val VALID_RANGE = -100.0..500.0

    fun build(): Topology {
        val builder = StreamsBuilder()
        val serde = SensorEventSerde.serde()

        val source = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), serde))
        val valid = source.filter { _, e -> isValid(e, VALID_RANGE) }
        val invalid = source.filterNot { _, e -> isValid(e, VALID_RANGE) }

        valid
            .mapValues { e -> normalize(e) }
            .to(OUTPUT_TOPIC, Produced.with(Serdes.String(), serde))

        invalid.to(DLQ_TOPIC, Produced.with(Serdes.String(), serde))

        return builder.build()
    }

    private fun isValid(event: SensorEvent, validRange: ClosedFloatingPointRange<Double>): Boolean {
        return event.sensorId.isNotBlank() && event.value in validRange
    }

    private fun normalize(event: SensorEvent): SensorEvent {
        val celsius = if (event.unit.equals("F", ignoreCase = true)) {
            (event.value - 32.0) * 5.0 / 9.0
        } else {
            event.value
        }
        val tags = if (celsius > 80.0) event.tags + "ANOMALY" else event.tags

        return event.copy(value = celsius, unit = "C", tags = tags)
    }
}
