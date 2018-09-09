package com.jacob.libraryservice.member.service.persistor

import com.jacob.libraryservice.domain.Member
import com.jacob.libraryservice.domain.envelope.UpsertMemberEvent
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
    lateinit var kafkaTemplate: KafkaTemplate<UUID, UpsertMemberEvent>
    @InjectMocks
    lateinit var memberPersistor: MemberPersistor

    @Test
    fun persist() {
        val member = Member(UUID.randomUUID(), "joe")
        given(kafkaTemplate.sendDefault(ArgumentMatchers.any(), argThat { it.newMemberData == member })).willReturn(AsyncResult(null))
        assertThat(memberPersistor.persist(member).block()?.newMemberData).isEqualTo(member)
    }
}