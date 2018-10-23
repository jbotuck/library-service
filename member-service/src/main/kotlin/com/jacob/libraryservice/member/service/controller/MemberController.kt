package com.jacob.libraryservice.member.service.controller

import com.jacob.libraryservice.domain.envelope.MemberProfileEvent
import com.jacob.libraryservice.domain.member.Member
import com.jacob.libraryservice.member.service.persistor.MemberPersistor
import com.jacob.libraryservice.member.service.search.MemberSearchService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/member")
class MemberController(private val memberPersistor: MemberPersistor, private val memberSearchService: MemberSearchService) {
    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: UUID): Mono<Member> {
        return memberSearchService.getOne(id)
    }

    @PostMapping
    fun create(@RequestBody member: Member): Mono<MemberProfileEvent> {
        return member.copy(id = UUID.randomUUID()).let {
            memberPersistor.persist(it)
        }
    }

    @PatchMapping("/{id}")
    fun update(@PathVariable("id") id: UUID, @RequestBody member: Member): Mono<MemberProfileEvent> {
        return member.copy(id = id).let { memberPersistor.persist(it) }
    }
}