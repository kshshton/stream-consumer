import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.errors.WakeupException
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties

class ConsumerService(
    private val bootstrapServers: String,
    private val topic: String,
    private val groupId: String
) {
    private val logger = LoggerFactory.getLogger(ConsumerService::class.java)

    @Volatile
    private var running = true

    fun start() {
        KafkaConsumer<String, String>(consumerProperties()).use { consumer ->
            val shutdownHook = Thread {
                running = false
                consumer.wakeup()
            }
            Runtime.getRuntime().addShutdownHook(shutdownHook)

            consumer.subscribe(listOf(topic))
            logger.info(
                "Consumer started. bootstrapServers={}, topic={}, groupId={}",
                bootstrapServers,
                topic,
                groupId
            )

            try {
                while (running) {
                    val records = consumer.poll(Duration.ofSeconds(1))
                    records.forEach { record ->
                        logger.info(
                            "partition={}, offset={}, key={}, value={}",
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value()
                        )
                    }
                }
            } catch (ex: WakeupException) {
                if (running) {
                    throw ex
                }
            } finally {
                try {
                    Runtime.getRuntime().removeShutdownHook(shutdownHook)
                } catch (_: IllegalStateException) {
                    // JVM is already shutting down.
                }
                logger.info("Consumer stopped.")
            }
        }
    }

    internal fun consumerProperties(): Properties =
        Properties().apply {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
            put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
            put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
        }
}
