# stream-consumer - Setup

Dockerized local stack for ingesting MQTT messages into Kafka via Kafka Connect.

## Stack

- Kafka (`confluentinc/cp-kafka:7.6.0`)
- Mosquitto (`eclipse-mosquitto:2`)
- Kafka Connect (custom image with MQTT connector plugin)

## Prerequisites

- WSL2
- Docker Desktop with WSL integration enabled
- `curl`

## Quick start (WSL2)

1. Start services:

```bash
docker compose up -d --build
```

2. Verify MQTT connector plugin:

```bash
curl http://localhost:8083/connector-plugins
```

Expected plugin class:
`io.confluent.connect.mqtt.MqttSourceConnector`

3. Register connector:

```bash
curl -X POST http://localhost:8083/connectors \
  -H "Content-Type: application/json" \
  --data-binary "@connect/mqtt-source-sensors.docker.json"
```

4. Consume from Kafka:

```bash
docker exec -it kafka kafka-console-consumer \
  --bootstrap-server kafka:29092 \
  --topic mqtt.sensors.raw \
  --from-beginning
```

## Useful checks

```bash
curl http://localhost:8083/connectors/mqtt-source-sensors/status
docker logs connect --tail 100
```

## Notes

- Use `connect/mqtt-source-sensors.docker.json` when Connect runs in Docker (uses `mosquitto:1883`).
- More context: `docs/mqtt-kafka-connect.md`.
