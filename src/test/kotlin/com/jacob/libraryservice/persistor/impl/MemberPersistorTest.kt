package com.jacob.libraryservice.persistor.impl

import com.jacob.libraryservice.domain.Member
import com.jacob.libraryservice.envelope.CreateMemberEvent
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.argThat
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.AsyncResult
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class MemberPersistorTest {
    @Mock
    lateinit var kafkaTemplate: KafkaTemplate<UUID, CreateMemberEvent>
    @InjectMocks
    lateinit var memberPersistor: MemberPersistor

    @Test
    fun persist() {
        val member = Member(UUID.randomUUID(), "joe")
        given(kafkaTemplate.sendDefault(ArgumentMatchers.any(), argThat { it.member == member })).willReturn(AsyncResult(null))
        assertThat(memberPersistor.persist(member).block()?.member).isEqualTo(member)
    }
}