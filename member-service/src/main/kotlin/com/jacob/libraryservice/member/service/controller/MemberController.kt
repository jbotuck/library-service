package com.jacob.libraryservice.member.service.controller

import com.jacob.libraryservice.domain.Member
import com.jacob.libraryservice.domain.envelope.UpsertMemberEvent
import com.jacob.libraryservice.member.service.persistor.MemberPersistor
import com.jacob.libraryservice.member.service.repository.MemberRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import java.util.*

@RestController("/member")
class MemberController(val memberPersistor: MemberPersistor, val memberRepository: MemberRepository) {
    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: UUID): Mono<Member> {
        return memberRepository.findById(id.toString()).switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)))
    }
    @PostMapping
    fun create(@RequestBody member: Member): Mono<UpsertMemberEvent> {
        return member.copy(id = UUID.randomUUID()).let {
            memberPersistor.persist(it)
        }
    }

    @PatchMapping("/{id}")
    fun update(@PathVariable("id") id: UUID, @RequestBody member: Member): Mono<UpsertMemberEvent> {
        return member.copy(id = id).let {
            memberPersistor.persist(it)
        }
    }
}