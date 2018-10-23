package com.jacob.libraryservice.domain.member

import com.jacob.libraryservice.domain.Entity
import com.jacob.libraryservice.domain.envelope.MemberEvent
import com.jacob.libraryservice.domain.envelope.MemberProfileEvent
import java.util.*

data class Member(override val id: UUID? = null, val memberData: MemberData, val events: List<MemberEvent> = emptyList()) : Entity {
    fun handle(event: MemberProfileEvent): Member {
        return copy(
                id = event.newMemberData.id,
                memberData = memberData.handle(event),
                events = events + event
        )
    }
}