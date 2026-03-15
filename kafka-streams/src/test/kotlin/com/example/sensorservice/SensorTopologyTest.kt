package com.example.sensorservice

import com.example.sensorservice.model.SensorEvent
import com.example.sensorservice.serde.SensorEventSerde
import com.example.sensorservice.topology.SimpleSensorTopology
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.TestInputTopic
import org.apache.kafka.streams.TestOutputTopic
import org.apache.kafka.streams.TopologyTestDriver
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SensorTopologyTest {
    @Test
    fun `valid events go to processed topic with normalization`() {
        val topology = SimpleSensorTopology.build()
        val props = java.util.Properties().apply {
            put(StreamsConfig.APPLICATION_ID_CONFIG, "sensor-topology-test")
            put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:9092")
        }

        TopologyTestDriver(topology, props).use { driver ->
            val serde = SensorEventSerde.serde()

            val input: TestInputTopic<String, SensorEvent> = driver.createInputTopic(
                SimpleSensorTopology.INPUT_TOPIC,
                Serdes.String().serializer(),
                serde.serializer()
            )
            val processed: TestOutputTopic<String, SensorEvent> = driver.createOutputTopic(
                SimpleSensorTopology.OUTPUT_TOPIC,
                Serdes.String().deserializer(),
                serde.deserializer()
            )
            val dlq: TestOutputTopic<String, SensorEvent> = driver.createOutputTopic(
                SimpleSensorTopology.DLQ_TOPIC,
                Serdes.String().deserializer(),
                serde.deserializer()
            )

            input.pipeInput("valid", SensorEvent("temp-01", 212.0, "F", 1L))

            val tempProcessed = processed.readValue()

            assertEquals("C", tempProcessed.unit)
            assertTrue(tempProcessed.value > 99.9 && tempProcessed.value < 100.1)
            assertTrue("ANOMALY" in tempProcessed.tags)
            assertTrue(dlq.isEmpty)
        }
    }

    @Test
    fun `invalid events go to dlq`() {
        val topology = SimpleSensorTopology.build()
        val props = java.util.Properties().apply {
            put(StreamsConfig.APPLICATION_ID_CONFIG, "sensor-topology-test-dlq")
            put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:9092")
        }

        TopologyTestDriver(topology, props).use { driver ->
            val serde = SensorEventSerde.serde()

            val input: TestInputTopic<String, SensorEvent> = driver.createInputTopic(
                SimpleSensorTopology.INPUT_TOPIC,
                Serdes.String().serializer(),
                serde.serializer()
            )
            val processed: TestOutputTopic<String, SensorEvent> = driver.createOutputTopic(
                SimpleSensorTopology.OUTPUT_TOPIC,
                Serdes.String().deserializer(),
                serde.deserializer()
            )
            val dlq: TestOutputTopic<String, SensorEvent> = driver.createOutputTopic(
                SimpleSensorTopology.DLQ_TOPIC,
                Serdes.String().deserializer(),
                serde.deserializer()
            )

            input.pipeInput("bad", SensorEvent("", 700.0, "C", 2L))

            assertTrue(processed.isEmpty)
            val dlqEvent = dlq.readValue()
            assertEquals(700.0, dlqEvent.value)
            assertEquals("", dlqEvent.sensorId)
        }
    }
}
