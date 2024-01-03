package pl.jakubtworek.notifications.kafka.listener

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import pl.jakubtworek.common.Constants
import pl.jakubtworek.notifications.kafka.MessageHandler
import pl.jakubtworek.notifications.model.message.LikeMessage

@Component
class LikeListener(
    private val messageHandler: MessageHandler,
    private val objectMapper: ObjectMapper
) {
    private val logger: Logger = LoggerFactory.getLogger(LikeListener::class.java)

    @KafkaListener(topics = [Constants.LIKE_TOPIC])
    fun process(message: String) {
        logger.info("Processing like message: $message")
        try {
            val likeMessage = message.deserializeToLike()
            messageHandler.processLikeMessage(likeMessage);
        } catch (e: Exception) {
            logger.error("Error processing like message", e)
            throw e
        }
    }

    private fun String.deserializeToLike(): LikeMessage =
        let { objectMapper.readValue(this, LikeMessage::class.java) }
}
