package com.jacob.libraryservice.member.service.controller

import com.jacob.libraryservice.domain.Member
import com.jacob.libraryservice.domain.envelope.UpsertMemberEvent
import com.jacob.libraryservice.member.service.persistor.MemberPersistor
import com.jacob.libraryservice.member.service.search.MemberSearchService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/member")
class MemberController(val memberPersistor: MemberPersistor, val memberSearchService: MemberSearchService) {
    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: UUID): Mono<Member> {
        return memberSearchService.getOne(id)
    }

    @PostMapping
    fun create(@RequestBody member: Member): Mono<UpsertMemberEvent> {
        return member.copyWithGeneratedId().let {
            memberPersistor.persist(it)
        }
    }

    @PatchMapping("/{id}")
    fun update(@PathVariable("id") id: UUID, @RequestBody member: Member): Mono<UpsertMemberEvent> {
        return member.also { it.id = id }.let { memberPersistor.persist(it) }
    }
}