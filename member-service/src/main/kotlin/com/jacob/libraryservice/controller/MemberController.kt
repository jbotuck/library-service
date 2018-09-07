package com.jacob.libraryservice.controller

import com.jacob.libraryservice.domain.Member
import com.jacob.libraryservice.domain.envelope.CreateMemberEvent
import com.jacob.libraryservice.persistor.Persistor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.*

@RestController("/member")
class MemberController(val memberPersistor: Persistor<Member>) {
    @PostMapping
    fun create(@RequestBody member: Member): Mono<CreateMemberEvent> {
        return member.copy(id = UUID.randomUUID()).let {
            memberPersistor.persist(it)
        }
    }
}