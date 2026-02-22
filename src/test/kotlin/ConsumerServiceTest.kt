import org.apache.kafka.clients.consumer.ConsumerConfig
import kotlin.test.Test
import kotlin.test.assertEquals

class ConsumerServiceTest {
    @Test
    fun `consumer properties include required kafka config`() {
        val service = ConsumerService(
            bootstrapServers = "localhost:9092",
            topic = "mqtt.sensors.raw",
            groupId = "mqtt-consumer-group"
        )

        val props = service.consumerProperties()

        assertEquals("localhost:9092", props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG])
        assertEquals("mqtt-consumer-group", props[ConsumerConfig.GROUP_ID_CONFIG])
        assertEquals("earliest", props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG])
        assertEquals("true", props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG])
    }
}
