fun main() {
    ConsumerService(
        "localhost:9092",
        "mqtt.sensors.raw",
        "mqtt-consumer-group"
    ).start()
}