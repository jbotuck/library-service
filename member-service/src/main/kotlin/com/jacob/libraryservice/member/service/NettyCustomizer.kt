package com.jacob.libraryservice.member.service

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.stereotype.Component

@Component
class NettyCustomizer : WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {
    override fun customize(factory: NettyReactiveWebServerFactory?) {
        System.setProperty("es.set.netty.runtime.available.processors", "false")
    }
}