package com.jacob.libraryservice.domain.envelope

import java.time.Instant
import java.util.*

data class Header(val eventId: UUID = UUID.randomUUID(), val originationTime: Instant = Instant.now())