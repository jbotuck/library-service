package com.jacob.libraryservice.envelope

import com.jacob.libraryservice.domain.Member

data class CreateMemberEvent(override val header: Header, val member: Member) : Event