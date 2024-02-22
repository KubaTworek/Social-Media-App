package pl.jakubtworek.gateway

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Configuration
open class GatewayConfig {

    private val logger = LoggerFactory.getLogger(GatewayConfig::class.java)

    @Bean
    open fun myRoutes(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
            .route { p ->
                p.path("/api/articles/**")
                    .filters { f ->
                        f.rewritePath("/api/(?<segment>.*)", "/\${segment}")
                        f.filter(createRequestLogger("ARTICLES"))
                        f.filter(createResponseLogger("ARTICLES"))
                    }
                    .uri("lb://ARTICLES")
            }
            .route { p ->
                p.path("/authors/api/**")
                    .filters { f ->
                        f.rewritePath("/authors/(?<segment>.*)", "/\${segment}")
                        f.filter(createRequestLogger("AUTHORS"))
                        f.filter(createResponseLogger("AUTHORS"))
                    }
                    .uri("lb://AUTHORS")
            }
            .route { p ->
                p.path("/auth/api/**")
                    .filters { f ->
                        f.rewritePath("/auth/(?<segment>.*)", "/\${segment}")
                        f.filter(createRequestLogger("AUTHORIZATION"))
                        f.filter(createResponseLogger("AUTHORIZATION"))
                    }
                    .uri("lb://AUTHORIZATION")
            }
            .route { p ->
                p.path("/notifications/api/**")
                    .filters { f ->
                        f.rewritePath("/notifications/(?<segment>.*)", "/\${segment}")
                        f.filter(createRequestLogger("NOTIFICATIONS"))
                        f.filter(createResponseLogger("NOTIFICATIONS"))
                    }
                    .uri("lb://NOTIFICATIONS")
            }
            .build()
    }

    private fun createRequestLogger(serviceName: String): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            logRequest(exchange, serviceName)
            chain.filter(exchange)
        }
    }

    private fun createResponseLogger(serviceName: String): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            chain.filter(exchange).then(Mono.fromRunnable {
                logResponse(exchange, serviceName)
            })
        }
    }

    private fun logRequest(exchange: ServerWebExchange, serviceName: String) {
        val request = exchange.request
        val headers = request.headers
        logger.info("Request to $serviceName: ${request.method} ${request.uri}, Headers: $headers")
    }

    private fun logResponse(exchange: ServerWebExchange, serviceName: String) {
        val response = exchange.response
        val headers = response.headers
        logger.info("Response from $serviceName: ${response.rawStatusCode}, Headers: $headers")
    }
}
