# Adding Business Logic for a New Sensor

This project uses a DSL to register sensor pipelines in one place.

Main file for pipeline registration:
- `src/main/kotlin/com/example/sensorservice/pipelines/SensorPipelines.kt`

DSL definitions:
- `src/main/kotlin/com/example/sensorservice/dsl/SensorPipelineDsl.kt`

## Quick flow

1. Open `SensorPipelines.defaults()`.
2. Add a new `sensor("your-sensor-id") { ... }` block.
3. Define topics and business rules (`filter`, `process`).
4. Add/adjust tests in `SensorTopologyTest`.

## Minimal template

```kotlin
sensor("pressure-03") {
    from("iot.pressure.raw")
    to("iot.pressure.processed")
    deadLetter("iot.pressure.dlq")

    filter { event ->
        event.value in 0.0..500.0
    }

    process { event ->
        val normalized = event.copy(
            // your business logic
            tags = if (event.value > 300.0) event.tags + "HIGH_PRESSURE" else event.tags
        )
        normalized
    }
}
```

## What each DSL part does

- `sensor("id")`: pipeline identity + routing by `event.sensorId`.
- `from(topic)`: input topic for this sensor.
- `to(topic)`: output topic for valid processed events.
- `deadLetter(topic)`: output topic for invalid events (failed filter).
- `filter { ... }`: validation/business gate. `false` sends to DLQ (if configured).
- `process { ... }`: core transformation/enrichment logic.

## Recommended pattern for business logic

Keep `filter` for hard validation and keep `process` for transformations:

- `filter`: range checks, required values, unit sanity.
- `process`: conversion, enrichment, tag assignment, derived values.

Example split:
- `filter`: `event.value in -40.0..125.0`
- `process`: Fahrenheit -> Celsius conversion, anomaly tag assignment.

## Test checklist

Update or add tests in:
- `src/test/kotlin/com/example/sensorservice/SensorTopologyTest.kt`

For each new sensor pipeline, verify:
- valid event goes to `to(...)` topic,
- invalid event goes to `deadLetter(...)` topic,
- business transformation in `process` is correct.

## Run locally

```bash
./gradlew test
./gradlew run
```

`run` starts `com.example.sensorservice.app.SensorStreamApplicationKt`.
