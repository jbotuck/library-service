package com.jacob.libraryservice.member.statemanager

import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest
@Ignore("this test requires a running kafka")
class StateManagerApplicationTest {
    @Test
    fun contextLoads() {
    }
}