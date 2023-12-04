package pl.jakubtworek.articles.kafka.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import pl.jakubtworek.articles.kafka.message.LikeMessage

@Service
class KafkaLikeService(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {
    fun sendLikeMessage(message: LikeMessage) =
        kafkaTemplate.send("t-like", message.serialize())

    private fun LikeMessage.serialize(): String =
        let(objectMapper::writeValueAsString)
}