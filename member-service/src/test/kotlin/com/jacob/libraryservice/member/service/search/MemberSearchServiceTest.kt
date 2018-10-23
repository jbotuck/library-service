package com.jacob.libraryservice.member.service.search

import com.fasterxml.jackson.databind.ObjectMapper
import com.jacob.libraryservice.domain.member.Member
import com.jacob.libraryservice.domain.member.MemberData
import io.searchbox.action.Action
import io.searchbox.client.JestClient
import io.searchbox.client.JestResult
import io.searchbox.client.JestResultHandler
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class MemberSearchServiceTest {
    @Mock
    private lateinit var objectMapper: ObjectMapper

    @Mock
    private lateinit var jestResult: JestResult

    @Suppress("UNCHECKED_CAST")
    private var jestClient: JestClient = object : JestClient {
        @Suppress("OverridingDeprecatedMember")
        override fun shutdownClient() {
        }

        override fun setServers(servers: MutableSet<String>?) {
        }

        override fun <T : JestResult?> executeAsync(clientRequest: Action<T>?, jestResultHandler: JestResultHandler<in T>?) {
            jestResultHandler?.completed(jestResult as T)
        }

        override fun close() {
        }

        override fun <T : JestResult?> execute(clientRequest: Action<T>?): T {
            return jestResult as T
        }
    }

    private lateinit var memberSearchService: MemberSearchService
    @Test
    fun getOne() {
        memberSearchService = MemberSearchService(jestClient, objectMapper)
        given(jestResult.sourceAsString).willReturn("a member record")
        given(objectMapper.readValue("a member record", Member::class.java))
                .willReturn(Member(memberData = MemberData("joe")))
        val result = memberSearchService.getOne(UUID.randomUUID()).block()
        assertThat(result?.memberData).isEqualTo(MemberData("joe"))
    }

    @Test
    fun getOne_notfound() {
        memberSearchService = MemberSearchService(jestClient, objectMapper)
        given(jestResult.sourceAsString).willReturn(null)
        assertThatThrownBy { memberSearchService.getOne(UUID.randomUUID()).block() }
                .isInstanceOf(ResponseStatusException::class.java).hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND)
    }
}