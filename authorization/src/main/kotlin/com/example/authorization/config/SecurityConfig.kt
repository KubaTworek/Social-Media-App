package com.example.authorization.config

import com.example.authorization.constants.SecurityConstants.JWT_HEADER
import com.example.authorization.constants.SecurityConstants.JWT_KEY
import com.example.authorization.filter.JWTTokenValidatorFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.*

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .cors()
            .configurationSource(corsConfigurationSource())
            .and()
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/user-info").authenticated()
                    .requestMatchers("/api/register", "/api/login").permitAll()
            }
            .httpBasic(Customizer.withDefaults())
            .addFilterBefore(
                JWTTokenValidatorFilter(JWT_HEADER, JWT_KEY),
                BasicAuthenticationFilter::class.java
            )

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowedOrigins = listOf("*")
        config.allowedMethods = listOf("*")
        config.allowCredentials = true
        config.allowedHeaders = listOf("*")
        config.exposedHeaders = listOf("Authorization")
        config.maxAge = 3600L

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
