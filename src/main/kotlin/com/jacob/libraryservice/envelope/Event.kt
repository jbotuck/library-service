package com.jacob.libraryservice.envelope

import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
interface Event {
    val header: Header
}