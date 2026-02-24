
fun main(args: Array<String>) {
    ConsumerService(
        bootstrapServers="localhost:9092",
        topic="mqtt.sensors.raw",
        groupId="mqtt-consumer-group",
        mqttSubtopicFilter=args
    ).start()
}