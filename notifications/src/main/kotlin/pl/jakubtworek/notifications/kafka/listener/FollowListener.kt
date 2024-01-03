package pl.jakubtworek.notifications.kafka.listener

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import pl.jakubtworek.common.Constants
import pl.jakubtworek.notifications.kafka.MessageHandler
import pl.jakubtworek.notifications.model.message.FollowMessage

@Component
class FollowListener(
    private val messageHandler: MessageHandler,
    private val objectMapper: ObjectMapper
) {
    private val logger: Logger = LoggerFactory.getLogger(FollowListener::class.java)

    @KafkaListener(topics = [Constants.FOLLOW_TOPIC])
    fun process(message: String) {
        logger.info("Processing follow message: $message")
        try {
            val followMessage = message.deserializeToFollow()
            messageHandler.processFollowMessage(followMessage);
        } catch (e: Exception) {
            logger.error("Error processing follow message", e)
            throw e
        }
    }

    private fun String.deserializeToFollow(): FollowMessage =
        let { objectMapper.readValue(this, FollowMessage::class.java) }
}
