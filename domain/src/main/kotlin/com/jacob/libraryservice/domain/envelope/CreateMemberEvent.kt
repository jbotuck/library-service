package com.jacob.libraryservice.domain.envelope

import com.jacob.libraryservice.domain.Member

data class CreateMemberEvent(override val header: Header, val member: Member) : Event