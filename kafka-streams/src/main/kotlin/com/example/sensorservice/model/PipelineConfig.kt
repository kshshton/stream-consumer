package com.example.sensorservice.model

data class RangeConfig(
    val min: Double,
    val max: Double
) {
    fun toClosedRange(): ClosedRange<Double> = min..max
}

data class PipelineConfig(
    val id: String,
    val inputTopic: String,
    val outputTopic: String,
    val dlqTopic: String,
    val validRange: RangeConfig,
    val expectedRange: RangeConfig
)

data class PipelineFileConfig(
    val pipelines: List<PipelineConfig>
)
