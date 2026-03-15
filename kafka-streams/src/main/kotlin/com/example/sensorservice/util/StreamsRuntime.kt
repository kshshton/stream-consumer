package com.example.sensorservice.util

import org.apache.kafka.streams.KafkaStreams

object StreamsRuntime {
    fun startAndAwaitShutdown(streams: KafkaStreams) {
        Runtime.getRuntime().addShutdownHook(Thread(streams::close))
        streams.start()
    }
}
