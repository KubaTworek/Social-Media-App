package pl.jakubtworek.gateway

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class GatewayConfig {

    @Bean
    open fun myRoutes(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
            .route { p ->
                p.path("/articles/api/**")
                    .filters { f ->
                        f.rewritePath("/articles/(?<segment>.*)", "/\${segment}")
                    }
                    .uri("lb://ARTICLES")
            }
            .route { p ->
                p.path("/authors/api/**")
                    .filters { f ->
                        f.rewritePath("/authors/(?<segment>.*)", "/\${segment}")
                    }
                    .uri("lb://AUTHORS")
            }
            .route { p ->
                p.path("/auth/api/**")
                    .filters { f ->
                        f.rewritePath("/auth/(?<segment>.*)", "/\${segment}")
                    }
                    .uri("lb://AUTHORIZATION")
            }
            .route { p ->
                p.path("/notifications/api/**")
                    .filters { f ->
                        f.rewritePath("/notifications/(?<segment>.*)", "/\${segment}")
                    }
                    .uri("lb://NOTIFICATIONS")
            }
            .build()
    }
}
