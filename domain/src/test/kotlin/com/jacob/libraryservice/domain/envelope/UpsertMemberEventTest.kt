package com.jacob.libraryservice.domain.envelope

import com.jacob.libraryservice.domain.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UpsertMemberEventTest {
    @Test
    fun testUpdate() {
        assertThat(UpsertMemberEvent(Header(), Member("joe").copyWithGeneratedId())
                .update(Member("jon", "j@aol.com").copyWithGeneratedId())).isEqualTo(Member("joe", "j@aol.com"))
        assertThat(UpsertMemberEvent(Header(), Member(null).copyWithGeneratedId())
                .update(Member("jon").copyWithGeneratedId()).name).isEqualTo("jon")
    }
}