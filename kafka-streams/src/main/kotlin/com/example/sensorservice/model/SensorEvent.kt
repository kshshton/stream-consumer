package com.example.sensorservice.model

import kotlinx.serialization.Serializable

@Serializable
data class SensorEvent(
    val sensorId: String,
    val value: Double,
    val unit: String,
    val timestamp: Long,
    val tags: List<String> = emptyList()
)
