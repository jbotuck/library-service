package com.jacob.libraryservice.member.service.controller

import com.jacob.libraryservice.domain.envelope.Header
import com.jacob.libraryservice.domain.envelope.MemberProfileEvent
import com.jacob.libraryservice.domain.member.Member
import com.jacob.libraryservice.domain.member.MemberData
import com.jacob.libraryservice.member.service.persistor.MemberPersistor
import com.jacob.libraryservice.member.service.search.MemberSearchService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.time.Instant
import java.util.*

@RunWith(SpringRunner::class)
@WebFluxTest(MemberController::class)
class MemberControllerTest {
    @Autowired
    private lateinit var client: WebTestClient
    @MockBean
    private lateinit var persistor: MemberPersistor
    @MockBean
    private lateinit var memberRepository: MemberSearchService
    private var dummyMember = Member(memberData = MemberData())

    @Test
    fun persist() {
        val memberToCreate = Member(memberData = MemberData("joe"))
        val returned = MemberProfileEvent(Header(UUID.randomUUID(), Instant.now()), Member(UUID.randomUUID(), MemberData("foo")))
        given(persistor.persist(ArgumentMatchers.argThat { it?.memberData == memberToCreate.memberData }
                ?: dummyMember)).willReturn(returned.toMono())
        client.post().uri("/member").accept(MediaType.APPLICATION_JSON_UTF8).syncBody(memberToCreate)
                .exchange()
                .expectBody<MemberProfileEvent>(MemberProfileEvent::class.java).returnResult().apply {
                    assertThat(this.responseBody).isEqualTo(returned)
                }
    }

    @Test
    fun getOne() {
        val arg1 = UUID.randomUUID()
        val expected = Member(UUID.randomUUID(), MemberData("foo"))
        given(memberRepository.getOne(arg1)).willReturn(Mono.just(expected))
        client.get().uri("/member/{id}", arg1)
                .exchange()
                .expectBody<Member>(Member::class.java).returnResult().apply {
                    assertThat(this.responseBody).isEqualTo(expected)
                }
    }

    @Test
    fun getOne_404() {
        given(memberRepository.getOne(ArgumentMatchers.any()
                ?: UUID.randomUUID())).willReturn(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)))
        client.get().uri("/member/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound
    }

}
