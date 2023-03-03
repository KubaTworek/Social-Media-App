package com.example.gateway

import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter
import org.springframework.cloud.gateway.filter.cors.CorsGatewayFilterApplicationListener
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.*

@Configuration
class GatewayConfig {

    @Bean
    fun myRoutes(builder: RouteLocatorBuilder): RouteLocator? {
        return builder.routes()
            .route { p: PredicateSpec ->
                p
                    .path("/articles/api/**")
                    .filters { f: GatewayFilterSpec ->
                        f
                            .rewritePath("/articles/(?<segment>.*)", "/\${segment}")
                    }
                    .uri("lb://ARTICLES")
            }.route { p: PredicateSpec ->
                p
                    .path("/authors/api/**")
                    .filters { f: GatewayFilterSpec ->
                        f
                            .rewritePath("/authors/(?<segment>.*)", "/\${segment}")
                    }
                    .uri("lb://AUTHORS")
            }.route { p: PredicateSpec ->
                p
                    .path("/auth/api/**")
                    .filters { f: GatewayFilterSpec ->
                        f
                            .rewritePath("/auth/(?<segment>.*)", "/\${segment}")
                    }
                    .uri("lb://AUTHORIZATION")
            }.build()
    }

/*    @Bean
    fun corsFilter(): WebFilter {
        return WebFilter { exchange: ServerWebExchange, chain: WebFilterChain ->
            val response = exchange.response
            response.headers.add("Access-Control-Allow-Origin", "*")
            response.headers.add("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS")
            response.headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization")
            if (exchange.request.method.name() === HttpMethod.OPTIONS.name()) {
                response.headers.add("Access-Control-Max-Age", "3600")
                response.setStatusCode(HttpStatus.OK)
                Mono.empty<Void>()
            } else {
                chain.filter(exchange)
            }
        }
    }*/
}