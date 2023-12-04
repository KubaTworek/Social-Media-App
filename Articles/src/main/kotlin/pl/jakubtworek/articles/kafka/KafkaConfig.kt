package pl.jakubtworek.articles.kafka

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaConfig {
    @Bean
    fun topicLike(): NewTopic = TopicBuilder
        .name("t-like")
        .partitions(1)
        .replicas(1)
        .build()
}