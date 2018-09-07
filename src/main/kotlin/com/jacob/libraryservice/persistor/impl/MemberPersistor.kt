package com.jacob.libraryservice.persistor.impl

import com.jacob.libraryservice.domain.Member
import com.jacob.libraryservice.envelope.CreateMemberEvent
import com.jacob.libraryservice.envelope.Event
import com.jacob.libraryservice.envelope.Header
import com.jacob.libraryservice.persistor.Persistor
import org.slf4j.LoggerFactory.getLogger
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.time.Instant
import java.util.*

@Component
class MemberPersistor(val kafkaTemplate: KafkaTemplate<UUID, Event>) : Persistor<Member> {
    companion object {
        @JvmStatic
        private val logger = getLogger(MemberPersistor::class.java)
    }

    override fun persist(entity: Member): Mono<CreateMemberEvent> {
        return CreateMemberEvent(Header(UUID.randomUUID(), Instant.now()), entity)
                .let { kafkaTemplate.sendDefault(entity.id, it).completable().toMono().ignoreElement().then(Mono.just(it)) }

    }
}