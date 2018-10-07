package com.jacob.libraryservice.member.service.persistor

import com.jacob.libraryservice.domain.envelope.Event
import com.jacob.libraryservice.domain.envelope.Header
import com.jacob.libraryservice.domain.envelope.UpsertMemberEvent
import com.jacob.libraryservice.domain.member.Member
import org.slf4j.LoggerFactory.getLogger
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.time.Instant
import java.util.*

@Component
class MemberPersistor(val kafkaTemplate: KafkaTemplate<String, Event<Member>>) {
    companion object {
        @JvmStatic
        private val logger = getLogger(MemberPersistor::class.java)
    }

    fun persist(entity: Member): Mono<UpsertMemberEvent> {
        return UpsertMemberEvent(Header(UUID.randomUUID(), Instant.now()), entity)
                .let {
                    kafkaTemplate.sendDefault(entity.id.toString(), it)
                            .completable().toMono().ignoreElement().then(it.toMono())
                }

    }
}