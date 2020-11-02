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

@EnableWebSecurity
@Configuration
class SecurityConfiguration(
        val userService: UserService
) : WebSecurityConfigurerAdapter() {

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
        source.registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues())
        return source
    }
}