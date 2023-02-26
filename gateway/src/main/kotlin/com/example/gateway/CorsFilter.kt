package com.example.gateway

import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class CorsFilter : GatewayFilter {

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val response = exchange.response
//        response.headers.add("Access-Control-Allow-Origin", "*")
        response.headers.add("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH")
        response.headers.add("Access-Control-Allow-Headers", "content-type,Vary,Vary,Vary,Access-Control-Allow-Origin,Access-Control-Allow-Methods,Access-Control-Allow-Headers,Access-Control-Max-Age,Allow,Content-Length,Date,Keep-Alive,Connection")
        println(exchange.request.method.name())
        println(HttpMethod.OPTIONS.name())
        if (exchange.request.method.name() == HttpMethod.OPTIONS.name()) {
            response.headers.add("Access-Control-Max-Age", "3600")
            response.statusCode = HttpStatus.OK
            return Mono.empty()
        }
        return chain.filter(exchange)
    }
}