package com.jacob.libraryservice.domain

import com.jacob.libraryservice.domain.envelope.UpsertMemberEvent
import java.util.*

data class Member(val name: String?, val email: String?) : Entity {
    constructor(name: String?) : this(name, null)

    override var id: UUID? = null
    fun copyWithGeneratedId(): Member {
        return copy().also { it.id = UUID.randomUUID() }
    }

    fun handleEvent(event: UpsertMemberEvent): Member {
        return copy(
                name = event.newMemberData.name ?: name,
                email = event.newMemberData.email ?: email
        ).also { it.id = id }
    }
}