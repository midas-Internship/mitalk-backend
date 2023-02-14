package com.example.mitalk.global.security

import com.example.mitalk.global.security.filter.FilterConfig
import com.example.mitalk.global.security.jwt.JwtTokenProvider
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain


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
<<<<<<< HEAD:src/main/kotlin/com/example/mitalk/global/security/SecurityConfig.kt
                .requestMatchers(RequestMatcher { request ->
                    CorsUtils.isPreFlightRequest(request)
                }).permitAll()
                .antMatchers("/file").permitAll()
                .antMatchers(HttpMethod.POST, "/customer/signin").permitAll()
                .antMatchers(HttpMethod.PATCH, "/auth").permitAll()
                .antMatchers("/auth/hello").hasAuthority(Role.CUSTOMER.name)
//                .antMatchers(HttpMethod.DELETE, "/auth").authenticated()
=======
>>>>>>> 674aeca63eea61a98d6405e812f4d96a2e87becc:src/main/kotlin/com/example/mitalk/global/security/jwt/SecurityConfig.kt

                .antMatchers(HttpMethod.POST, "/customer/signin").permitAll()

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