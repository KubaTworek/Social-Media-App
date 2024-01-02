package pl.jakubtworek.articles.kafka.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import pl.jakubtworek.articles.kafka.message.ArticleMessage
import pl.jakubtworek.articles.kafka.message.LikeMessage


@Service
class KafkaLikeService(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {
    private val logger: Logger = LoggerFactory.getLogger(KafkaLikeService::class.java)

    fun sendLikeMessage(message: LikeMessage) {
        val serializedMessage = message.serialize()
        logger.info("Sending like message to Kafka topic t-like: $serializedMessage")
        kafkaTemplate.send("t-like", serializedMessage)
        logger.info("Like message sent successfully")
    }

    fun sendArticleMessage(message: ArticleMessage) {
        val serializedMessage = message.serialize()
        logger.info("Sending article message to Kafka topic t-article: $serializedMessage")
        kafkaTemplate.send("t-article", serializedMessage)
        logger.info("Article message sent successfully")
    }

    private fun LikeMessage.serialize(): String =
        let(objectMapper::writeValueAsString)

    private fun ArticleMessage.serialize(): String =
        let(objectMapper::writeValueAsString)
}
