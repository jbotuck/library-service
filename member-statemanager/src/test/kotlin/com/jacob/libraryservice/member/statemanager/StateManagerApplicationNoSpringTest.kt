package com.jacob.libraryservice.member.statemanager

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.jacob.libraryservice.domain.Member
import com.jacob.libraryservice.domain.envelope.Header
import com.jacob.libraryservice.domain.envelope.UpsertMemberEvent
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.TopologyTestDriver
import org.apache.kafka.streams.test.ConsumerRecordFactory
import org.apache.kafka.streams.test.OutputVerifier
import org.assertj.core.api.Assertions
import org.junit.Test
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import java.util.*

class StateManagerApplicationNoSpringTest {
    private val objectMapper = ObjectMapper().apply {
        registerModule(KotlinModule())
        registerModule(JavaTimeModule())
    }
    private val stateManagerApplication = StateManagerApplication("foo", objectMapper)
    @Test
    fun testKtable() {
        val topologyTestDriver = TopologyTestDriver(StreamsBuilder().let {
            stateManagerApplication.memberTable(stateManagerApplication.eventStream(it))
            it.build()
        }, Properties().apply {
            put(StreamsConfig.APPLICATION_ID_CONFIG, "member")
            put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "foo")
            put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde::class.java)
        })
        Assertions.assertThat(topologyTestDriver.allStateStores.size).isEqualTo(1)
        val factory: ConsumerRecordFactory<String, UpsertMemberEvent> = ConsumerRecordFactory(StringSerializer(), JsonSerializer<UpsertMemberEvent>(objectMapper))
        val newMemberData = Member("joe").copyWithGeneratedId()
        topologyTestDriver.pipeInput(factory.create("MEMBER.EVENT", newMemberData.id.toString(), UpsertMemberEvent(Header(), newMemberData)))
        val outputRecord: ProducerRecord<String, Member> = topologyTestDriver.readOutput("MEMBER.TABLE", StringDeserializer(), JsonDeserializer<Member>(Member::class.java, objectMapper))
        OutputVerifier.compareKeyValue(outputRecord, newMemberData.id.toString(), newMemberData) // throws AssertionError if key or value does not match
    }
}