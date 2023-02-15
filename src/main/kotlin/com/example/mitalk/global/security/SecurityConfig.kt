package com.example.mitalk.global.security

import com.example.mitalk.domain.auth.domain.Role
import com.example.mitalk.global.security.filter.FilterConfig
import com.example.mitalk.global.security.jwt.JwtTokenProvider
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsUtils


@Configuration
class SecurityConfig(
        private val jwtTokenProvider: JwtTokenProvider,
        private val objectMapper: ObjectMapper,
) {
    @Bean
    protected fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
                .cors().and()
                .csrf().disable()
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http
                .authorizeRequests()
                .requestMatchers(RequestMatcher { request ->
                    CorsUtils.isPreFlightRequest(request)
                }).permitAll()
                .antMatchers(HttpMethod.POST, "/file").authenticated()
                .antMatchers(HttpMethod.POST, "/customer/signin").permitAll()
                .antMatchers(HttpMethod.PATCH, "/auth").permitAll()
                .antMatchers("/auth/hello").hasAuthority(Role.CUSTOMER.name)
                .antMatchers(HttpMethod.POST, "/customer/signin").permitAll()
                .antMatchers(HttpMethod.GET, "/customer/question").authenticated()
                .antMatchers(HttpMethod.POST, "/customer/review").authenticated()
                .antMatchers(HttpMethod.GET, "/customer/review").authenticated()

                .antMatchers(HttpMethod.PATCH, "/auth").permitAll()
                .antMatchers(HttpMethod.DELETE, "/auth").authenticated()

                .antMatchers("/ws/chat").permitAll()
                .anyRequest().denyAll()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(CustomAuthenticationEntryPoint(objectMapper))

                .and()
                .apply(FilterConfig(jwtTokenProvider, objectMapper))

        return http.build()
    }

}