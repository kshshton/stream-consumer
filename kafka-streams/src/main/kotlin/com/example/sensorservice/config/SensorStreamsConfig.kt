package com.example.sensorservice.config

import org.apache.kafka.streams.StreamsConfig
import java.util.Properties

object SensorStreamsConfig {
    fun create(
        applicationId: String = "sensor-stream-service",
        bootstrapServers: String = "localhost:9092"
    ): Properties {
        return Properties().apply {
            put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId)
            put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
            put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1_000)
            put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0)
        }
    }
}
