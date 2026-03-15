package com.example.sensorservice.config

import com.example.sensorservice.model.PipelineFileConfig
import com.example.sensorservice.model.PipelineConfig
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule

object PipelineConfigLoader {
    private val mapper = ObjectMapper(YAMLFactory())
        .registerModule(KotlinModule.Builder().build())

    fun load(resource: String = "/pipelines.yml"): List<PipelineConfig> {
        val stream = PipelineConfigLoader::class.java.getResourceAsStream(resource)
            ?: error("Missing resource: $resource")
        return mapper.readValue(stream, PipelineFileConfig::class.java).pipelines
    }
}
