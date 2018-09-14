package com.jacob.libraryservice.domain.envelope

import com.jacob.libraryservice.domain.Member

class UpsertMemberEvent(override val header: Header, val newMemberData: Member) : MemberEvent {
    override fun update(existing: Member?): Member {
        return existing?.handleEvent(this) ?: newMemberData
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UpsertMemberEvent

        if (header != other.header) return false
        if (newMemberData != other.newMemberData) return false

        return true
    }

    override fun hashCode(): Int {
        var result = header.hashCode()
        result = 31 * result + newMemberData.hashCode()
        return result
    }

}