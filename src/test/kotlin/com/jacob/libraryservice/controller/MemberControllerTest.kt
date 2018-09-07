package com.jacob.libraryservice.controller

import com.jacob.libraryservice.domain.Member
import com.jacob.libraryservice.envelope.CreateMemberEvent
import com.jacob.libraryservice.envelope.Header
import com.jacob.libraryservice.persistor.Persistor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.toMono
import java.time.Instant
import java.util.*

@RunWith(SpringRunner::class)
@WebFluxTest(MemberController::class)
class MemberControllerTest {
    @Autowired
    lateinit var client: WebTestClient
    @MockBean
    lateinit var persistor: Persistor<Member>
    var dummyMember = Member(null, null)

    @Test
    fun persist() {
        val memberToCreate = Member(null, "joe")
        val returned = CreateMemberEvent(Header(UUID.randomUUID(), Instant.now()), Member(UUID.randomUUID(), "foo"))
        given(persistor.persist(ArgumentMatchers.argThat { it?.name == "joe" }
                ?: dummyMember)).willReturn(returned.toMono())
        client.post().uri("/member").accept(MediaType.APPLICATION_JSON_UTF8).syncBody(memberToCreate)
                .exchange()
                .expectBody<CreateMemberEvent>(CreateMemberEvent::class.java).returnResult().apply {
                    assertThat(this.responseBody).isEqualTo(returned)
                }
    }
}
