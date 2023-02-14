package com.example.mitalk.global.security

import com.example.mitalk.domain.auth.domain.Role
import com.example.mitalk.global.config.FilterConfig
import com.example.mitalk.global.security.CustomAuthenticationEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.example.mitalk.global.security.jwt.JwtTokenProvider
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsUtils

@Configuration
@EnableWebSecurity
class SecurityConfig(
        private val jwtTokenProvider: JwtTokenProvider,
        private val objectMapper: ObjectMapper,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
                .cors().and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .requestMatchers(RequestMatcher { request ->
                    CorsUtils.isPreFlightRequest(request)
                }).permitAll()
                .antMatchers(HttpMethod.POST, "/customer/signin").permitAll()
                .antMatchers(HttpMethod.PATCH, "/auth").permitAll()
                .antMatchers("/auth/hello").hasAuthority(Role.CUSTOMER.name)
//                .antMatchers(HttpMethod.DELETE, "/auth").authenticated()

                .anyRequest().denyAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(CustomAuthenticationEntryPoint(objectMapper))

                .and()
                .apply(FilterConfig(jwtTokenProvider, objectMapper))
                .and()
                .build()
    }

}