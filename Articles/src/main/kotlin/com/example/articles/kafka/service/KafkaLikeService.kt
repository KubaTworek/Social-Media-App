package com.example.articles.kafka.service

import com.example.articles.kafka.message.LikeMessage
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

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