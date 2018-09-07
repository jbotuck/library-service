package com.jacob.libraryservice.domain.envelope

import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
interface Event {
    val header: Header
}