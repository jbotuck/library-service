package com.jacob.libraryservice.domain.member

import com.jacob.libraryservice.domain.envelope.MemberProfileEvent

data class MemberData(val name: String? = null, val email: String? = null) {
    fun handle(event: MemberProfileEvent): MemberData {
        return copy(
                name = event.newMemberData.memberData.name ?: name,
                email = event.newMemberData.memberData.email ?: email
        )
    }
}