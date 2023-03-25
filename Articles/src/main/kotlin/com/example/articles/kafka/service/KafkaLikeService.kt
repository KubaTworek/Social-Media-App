package com.example.articles.kafka.service

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaLikeService(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    fun sendLikeMessage(message: String) =
        kafkaTemplate.send("t-like", message)
}