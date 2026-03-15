package com.example.sensorservice.app

import com.example.sensorservice.config.PipelineConfigLoader
import com.example.sensorservice.config.SensorStreamsConfig
import com.example.sensorservice.topology.GenericSensorTopology
import com.example.sensorservice.util.StreamsRuntime
import org.apache.kafka.streams.KafkaStreams

fun main() {
    val pipelines = PipelineConfigLoader.load()
    val props = SensorStreamsConfig.create()

    val streams = pipelines.map { cfg ->
        KafkaStreams(
            GenericSensorTopology(
                inputTopic = cfg.inputTopic,
                outputTopic = cfg.outputTopic,
                dlqTopic = cfg.dlqTopic,
                validRange = cfg.validRange.toClosedRange(),
                expectedRange = cfg.expectedRange.toClosedRange()
            ).build(),
            props
        )
    }

    streams.forEach(StreamsRuntime::startAndAwaitShutdown)
}
