package com.jacob.libraryservice.persistor

import com.jacob.libraryservice.domain.envelope.CreateMemberEvent
import reactor.core.publisher.Mono

interface Persistor<T> {
    fun persist(entity: T): Mono<CreateMemberEvent>
}