# stream-consumer - Telemetry Processing Plan

## Goal

The goal of this system is to process telemetry data from sensors in real time, generate alerts, and store historical data for long-term analytics.

## Data Flow

```text
sensors
  ↓
Kafka topic: telemetry_raw
  ↓
stream processing
  ├─ alert_topic
  ├─ realtime_metrics
  └─ telemetry_clean
          ↓
      TimescaleDB
```

## Processing Features

### 1. Sensor-Based Filtering (Kafka / Stream Processor)

Reason:
- reduce data volume
- route events to different topics

Example:

```text
telemetry_raw -> filter(engine sensors) -> engine_topic
```

### 2. Unit Conversion (Kafka)

Conversion examples:
- Fahrenheit -> Celsius
- psi -> bar

### 3. Anomaly Detection (Kafka, fixed threshold)

Why:
- must run in real time
- generates alerts immediately

Example:

```text
sensor_stream -> threshold check -> alert_topic
```

### 4. Multiple Window Types (Kafka Streams)

Examples:
- average temperature over 5s window
- average temperature over the last hour

### 5. Aggregations (Kafka / Kafka Streams)

Examples:
- max temperature over the last 10s
- min temperature over the last 10s
- average RPM over 1 minute

### 6. Platform Optimization (Kafka)

Scope:
- partitioning per sensor
- replication
- exactly-once semantics
- event time vs processing time

## Historical and Analytics Layer (TimescaleDB)

### 1. Historical Data After Sensors Go Offline

TimescaleDB stores data long term and supports more complex queries, for example:

```sql
SELECT avg(temp)
FROM telemetry
WHERE sensor_id = 10
  AND time > now() - interval '7 days';
```

### 2. Retention (Kafka / TimescaleDB)

Strategy:
- Kafka: 7 days (or another short operational window)
- TimescaleDB: months / years

### 3. Long-Term Aggregations (TimescaleDB)

Examples:
- average temperature per hour
- average temperature per day
- trend analysis using continuous aggregates

## Architecture Summary

Kafka and Kafka Streams handle the real-time layer:
- filtering
- conversions
- anomaly detection
- windowed aggregations

TimescaleDB handles the historical layer:
- long-term storage
- advanced analytical queries
- long-horizon aggregations and trends
