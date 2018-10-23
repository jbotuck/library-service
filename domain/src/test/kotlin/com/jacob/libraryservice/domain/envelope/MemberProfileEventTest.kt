package com.jacob.libraryservice.domain.envelope

import com.jacob.libraryservice.domain.member.Member
import com.jacob.libraryservice.domain.member.MemberData
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MemberProfileEventTest {
    @Test
    fun testUpdate() {
        assertThat(MemberProfileEvent(Header(), Member(memberData = MemberData("joe")))
                .process(Member(memberData = MemberData("jon", "j@aol.com"))).memberData)
                .isEqualTo(MemberData("joe", "j@aol.com"))
    }
}