package com.ongdev.request.configs

import com.ongdev.request.services.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*
import java.util.Collections.unmodifiableList

@EnableWebSecurity
@Configuration
class SecurityConfiguration(
        val userService: UserService
) : WebSecurityConfigurerAdapter() {
    companion object {
        private val ALLOWED_ALL = unmodifiableList(listOf(CorsConfiguration.ALL))
        private val ALLOWED_ORIGINS = ALLOWED_ALL
        private val ALLOWED_METHODS = unmodifiableList(listOf(
                HttpMethod.GET.name,
                HttpMethod.HEAD.name,
                HttpMethod.POST.name,
                HttpMethod.PUT.name,
                HttpMethod.DELETE.name))
        private val ALLOWED_HEADERS = ALLOWED_ALL
    }

    @Bean
    fun tokenAuthenticationFilter(): TokenAuthenticationFilter {
        return TokenAuthenticationFilter(userService)
    }

    override fun configure(http: HttpSecurity?) {
        http
                ?.cors()
                ?.and()
                ?.csrf()?.disable()
                ?.formLogin()?.disable()
                ?.httpBasic()?.disable()
                ?.exceptionHandling()?.authenticationEntryPoint(RestAuthenticationEntryPoint())
                ?.and()
                ?.authorizeRequests()
                ?.antMatchers(HttpMethod.GET, "/videos")?.permitAll()
                ?.antMatchers(HttpMethod.GET, "/udemy")?.permitAll()
                ?.antMatchers(HttpMethod.GET, "/qna")?.permitAll()
                ?.antMatchers("/actuator/**")?.permitAll()
                ?.antMatchers("/swagger-ui.html", "/swagger-ui/**")?.permitAll()
                ?.antMatchers("/swagger-resources/**")?.permitAll()
                ?.antMatchers("/v3/api-docs/**")?.permitAll()
                ?.anyRequest()?.authenticated()
        http?.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowedOrigins = ALLOWED_ORIGINS
        corsConfiguration.allowedMethods = ALLOWED_METHODS
        corsConfiguration.allowedHeaders = ALLOWED_HEADERS
        source.registerCorsConfiguration(
                "/**",
                corsConfiguration)
        return source
    }
}