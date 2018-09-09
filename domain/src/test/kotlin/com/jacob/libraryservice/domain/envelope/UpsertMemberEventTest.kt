package com.jacob.libraryservice.domain.envelope

import com.jacob.libraryservice.domain.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class UpsertMemberEventTest {
    @Test
    fun testUpdate() {
        assertThat(UpsertMemberEvent(Header(), Member(UUID.randomUUID(), "joe"))
                .update(Member(UUID.randomUUID(), "jon")).name).isEqualTo("joe")
        assertThat(UpsertMemberEvent(Header(), Member(UUID.randomUUID(), null))
                .update(Member(UUID.randomUUID(), "jon")).name).isEqualTo("jon")
    }
}