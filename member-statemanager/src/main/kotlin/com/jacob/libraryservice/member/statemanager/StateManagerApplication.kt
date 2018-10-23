package com.jacob.libraryservice.member.statemanager

import com.fasterxml.jackson.databind.ObjectMapper
import com.jacob.libraryservice.domain.envelope.MemberEvent
import com.jacob.libraryservice.domain.member.Member
import com.jacob.libraryservice.domain.member.MemberData
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.Consumed
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.errors.LogAndFailExceptionHandler
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.kstream.Produced
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.support.serializer.JsonSerde
import java.util.*


@SpringBootApplication
@EnableKafkaStreams
class StateManagerApplication(@Value("\${brokerAddresses:localhost:9092}") val brokerAddresses: String, objectMapper: ObjectMapper) {
    private val memberSerde = JsonSerde<Member>(Member::class.java, objectMapper)
    private val memberEventSerde = JsonSerde<MemberEvent>(MemberEvent::class.java, objectMapper)

    @Bean
    fun defaultKafkaStreamsConfig(): StreamsConfig {
        return StreamsConfig(Properties().apply {
            put(StreamsConfig.APPLICATION_ID_CONFIG, "member")
            put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokerAddresses)
            put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde::class.java)
            put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndFailExceptionHandler::class.java)
        })
    }

    @Bean
    fun eventStream(streamsBuilder: StreamsBuilder): KStream<String, MemberEvent> {
        return streamsBuilder.stream<String, MemberEvent>("MEMBER.EVENT", Consumed.with(null, memberEventSerde))
    }

    @Bean
    fun memberTable(eventStream: KStream<String, MemberEvent>): KTable<String, Member> {
        return eventStream.groupByKey().aggregate({ Member(memberData = MemberData()) }, { _, event, member -> event.process(member) }, Materialized.with(null, memberSerde))
                .apply {
                    toStream().through("MEMBER.TABLE", Produced.with(null, memberSerde))
                            .to("MEMBER.VIEW.ELASTIC", Produced.with(null, memberSerde))
                }
    }
}

fun main(args: Array<String>) {
    runApplication<StateManagerApplication>(*args)
}