package pl.jakubtworek.notifications.kafka.listener

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import pl.jakubtworek.common.Constants
import pl.jakubtworek.notifications.kafka.MessageHandler
import pl.jakubtworek.notifications.model.message.ArticleMessage

@Component
class ArticleListener(
    private val messageHandler: MessageHandler,
    private val objectMapper: ObjectMapper
) {
    private val logger: Logger = LoggerFactory.getLogger(ArticleListener::class.java)

    @KafkaListener(topics = [Constants.ARTICLE_TOPIC])
    fun process(message: String) {
        logger.info("Processing article message: $message")
        try {
            val articleMessage = message.deserializeToArticle()
            messageHandler.processArticleMessage(articleMessage);
        } catch (e: Exception) {
            logger.error("Error processing article message", e)
            throw e
        }
    }

    private fun String.deserializeToArticle(): ArticleMessage =
        let { objectMapper.readValue(this, ArticleMessage::class.java) }
}
