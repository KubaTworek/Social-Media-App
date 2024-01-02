package pl.jakubtworek.authors.kafka

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaConfig {
    @Bean
    fun topicFollow(): NewTopic = TopicBuilder
        .name("t-follow")
        .partitions(1)
        .replicas(1)
        .build()
}
