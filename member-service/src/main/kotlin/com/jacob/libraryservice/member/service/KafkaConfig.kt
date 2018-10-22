package com.jacob.libraryservice.member.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaConfig(val properties: KafkaProperties, val objectMapper: ObjectMapper) {
    @Bean//need to use the spring configured objectMapper for correct java8 time serialization
    fun kafkaProducerFactory(): ProducerFactory<*, *> {
        return DefaultKafkaProducerFactory<Any, Any>(properties.buildProducerProperties()).apply {
            setValueSerializer(JsonSerializer(objectMapper))
        }
    }
}