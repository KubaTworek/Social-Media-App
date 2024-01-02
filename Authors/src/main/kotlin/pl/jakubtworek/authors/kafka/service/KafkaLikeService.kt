package pl.jakubtworek.authors.kafka.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import pl.jakubtworek.authors.kafka.message.FollowMessage

@Service
class KafkaLikeService(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {
    private val logger: Logger = LoggerFactory.getLogger(KafkaLikeService::class.java)

    fun sendFollowMessage(message: FollowMessage) {
        val serializedMessage = message.serialize()
        logger.info("Sending follow message to Kafka topic t-like: $serializedMessage")
        kafkaTemplate.send("t-follow", serializedMessage)
        logger.info("Follow message sent successfully")
    }

    private fun FollowMessage.serialize(): String =
        let(objectMapper::writeValueAsString)
}
