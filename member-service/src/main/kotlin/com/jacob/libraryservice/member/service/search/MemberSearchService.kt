package com.jacob.libraryservice.member.service.search

import com.fasterxml.jackson.databind.ObjectMapper
import com.jacob.libraryservice.domain.member.Member
import io.searchbox.client.JestClient
import io.searchbox.client.JestResult
import io.searchbox.client.JestResultHandler
import io.searchbox.core.Get
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import java.util.*

@Component
class MemberSearchService(val jestClient: JestClient, private val objectMapper: ObjectMapper) {

    fun getOne(id: UUID): Mono<Member> {
        return Mono.create<Member> { sink ->
            jestClient.executeAsync(Get.Builder("member", id.toString()).build(), object : JestResultHandler<JestResult> {
                override fun completed(result: JestResult) {
                    result.sourceAsString?.let { sink.success(objectMapper.readValue(it, Member::class.java)) }
                            ?: sink.error(ResponseStatusException(HttpStatus.NOT_FOUND))
                }

                override fun failed(ex: Exception) {
                    sink.error(ex)
                }
            })
        }
    }
}