package com.example.sensorservice.serde

import com.example.sensorservice.model.SensorEvent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.Serializer

object SensorEventSerde {
    private val json = Json { ignoreUnknownKeys = true }

    fun serde(): Serde<SensorEvent> {
        return Serdes.serdeFrom(serializer(), deserializer())
    }

    private fun serializer(): Serializer<SensorEvent> {
        return Serializer { _, event -> event?.let { toJson(it).encodeToByteArray() } }
    }

    private fun deserializer(): Deserializer<SensorEvent> {
        return Deserializer { _, bytes -> bytes?.let { fromJson(it.decodeToString()) } }
    }

    private fun toJson(event: SensorEvent): String {
        return json.encodeToString(event)
    }

    private fun fromJson(payload: String): SensorEvent {
        return json.decodeFromString(payload)
    }
}
