package com.jacob.libraryservice.domain.envelope

import com.jacob.libraryservice.domain.member.Member

data class UpsertMemberEvent(override val header: Header, val newMemberData: Member) : MemberEvent {
    override fun process(existing: Member?): Member {
        return existing?.handle(this)
                ?: newMemberData.handle(this)//todo update stateManager to use default empty member and simplify this
    }
}