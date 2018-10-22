package com.jacob.libraryservice.member.service.search

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
class MemberSearchService(val jestClient: JestClient) {
    fun getOne(id: UUID): Mono<Member> {
        return Mono.create<Member> { sink ->
            jestClient.executeAsync(Get.Builder("member", id.toString()).build(), object : JestResultHandler<JestResult> {
                override fun completed(result: JestResult) {
                    sink.success(result.getSourceAsObject(Member::class.java))//TODO this fails as JEST works based in GSON which doesn't know how to deserialize the member
                }

                override fun failed(ex: Exception) {
                    sink.error(ex)
                }
            })
        }.switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)))//TODO this turns all errors into 404, not good
    }
}