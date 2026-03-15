package com.example.configpipeline.topology

import com.example.configpipeline.config.*
import com.example.configpipeline.model.SensorEvent
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.TopologyTestDriver
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ConfigDrivenTopologyTest {

    private lateinit var testDriver: TopologyTestDriver
    private val json = Json { ignoreUnknownKeys = true }

    private val testConfig = ConfigDrivenServiceConfig(
        app = AppConfig("test-app", "localhost:9092"),
        pipelines = listOf(
            SensorPipelineConfig(
                name = "temp-test",
                sensorId = "temp-01",
                inputTopic = "input",
                outputTopic = "output",
                errorTopic = "error",
                validation = ValidationRules(minValue = 0.0, maxValue = 100.0, allowedUnits = listOf("C", "F")),
                processing = listOf(
                    ProcessingStep(
                        type = StepType.UNIT_CONVERSION,
                        fromUnit = "F",
                        toUnit = "C",
                        factor = 0.5, // Simplified for testing: (F * 0.5) + 0
                        offset = 0.0
                    ),
                    ProcessingStep(
                        type = StepType.THRESHOLD_TAG,
                        threshold = 40.0,
                        operator = "gt",
                        tag = "HOT"
                    )
                )
            )
        )
    )

    @BeforeEach
    fun setUp() {
        val props = Properties()
        props[StreamsConfig.APPLICATION_ID_CONFIG] = "test"
        props[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = "dummy:1234"
        testDriver = TopologyTestDriver(buildConfigDrivenTopology(testConfig), props)
    }

    @AfterEach
    fun tearDown() {
        testDriver.close()
    }

    @Test
    fun `should process valid event with conversion and tagging`() {
        val inputTopic = testDriver.createInputTopic("input", Serdes.String().serializer(), Serdes.String().serializer())
        val outputTopic = testDriver.createOutputTopic("output", Serdes.String().deserializer(), Serdes.String().deserializer())

        val event = SensorEvent("temp-01", 90.0, "F", System.currentTimeMillis())
        inputTopic.pipeInput("key", json.encodeToString(event))

        val result = json.decodeFromString<SensorEvent>(outputTopic.readValue())
        assertEquals(45.0, result.value) // 90.0 * 0.5
        assertEquals("C", result.unit)
        assertTrue(result.tags.contains("HOT"))
    }

    @Test
    fun `should send invalid event to error topic`() {
        val inputTopic = testDriver.createInputTopic("input", Serdes.String().serializer(), Serdes.String().serializer())
        val errorTopic = testDriver.createOutputTopic("error", Serdes.String().deserializer(), Serdes.String().deserializer())

        // Wrong sensor ID
        val event = SensorEvent("wrong-id", 50.0, "C", System.currentTimeMillis())
        inputTopic.pipeInput("key", json.encodeToString(event))

        val result = errorTopic.readValue()
        assertTrue(result.contains("wrong-id"))
    }
}
