package com.example.gateway

import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
class CorsFilter : GlobalFilter, Ordered {

    private val logger: Logger = LoggerFactory.getLogger(CorsFilter::class.java)

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        logger.info("Executing CORS filter")

        val response = exchange.response
        response.headers.add("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH")
        response.headers.add("Access-Control-Allow-Headers", "content-type,Vary,Vary,Vary,Access-Control-Allow-Origin,Access-Control-Allow-Methods,Access-Control-Allow-Headers,Access-Control-Max-Age,Allow,Content-Length,Date,Keep-Alive,Connection")

        if (exchange.request.method == HttpMethod.OPTIONS) {
            response.headers.add("Access-Control-Max-Age", "3600")
            response.statusCode = HttpStatus.OK
            logger.info("CORS preflight request handled successfully")
            return Mono.empty()
        }

        logger.info("Passing request down the filter chain")
        return chain.filter(exchange)
    }

    override fun getOrder(): Int {
        return Ordered.HIGHEST_PRECEDENCE
    }
}
