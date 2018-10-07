package com.jacob.libraryservice.domain.envelope

import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
interface Event<T> {
    val header: Header
    fun process(existing: T?): T
}