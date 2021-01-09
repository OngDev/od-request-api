package com.ongdev.request.configs

import org.keycloak.adapters.springsecurity.KeycloakConfiguration
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.keycloak.adapters.springsecurity.management.HttpSessionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.Collections.unmodifiableList

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
class SecurityConfiguration() : KeycloakWebSecurityConfigurerAdapter() {
    companion object {
        // Consider moving those values to Env vars
        private val ALLOWED_ALL = unmodifiableList(listOf(CorsConfiguration.ALL))
        private val ALLOWED_ORIGINS = ALLOWED_ALL
        private val ALLOWED_METHODS = unmodifiableList(listOf(
                HttpMethod.GET.name,
                HttpMethod.HEAD.name,
                HttpMethod.POST.name,
                HttpMethod.PUT.name,
                HttpMethod.DELETE.name))
        private val ALLOWED_HEADERS = ALLOWED_ALL
        private const val MAX_AGE = 1800L
    }

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        val simpleAuthorityMapper = SimpleAuthorityMapper()

        val keycloakAuthenticationProvider = keycloakAuthenticationProvider()
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(simpleAuthorityMapper)
        auth.authenticationProvider(keycloakAuthenticationProvider)
    }

    override fun configure(http: HttpSecurity?) {
        http
                ?.cors()
                ?.and()
                ?.csrf()?.disable()
                ?.formLogin()?.disable()
                ?.httpBasic()?.disable()
                ?.authorizeRequests()
                ?.anyRequest()
                ?.permitAll()
    }

    @Bean
    override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy {
        return RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowedOrigins = ALLOWED_ORIGINS
        corsConfiguration.allowedMethods = ALLOWED_METHODS
        corsConfiguration.allowedHeaders = ALLOWED_HEADERS
        corsConfiguration.maxAge = MAX_AGE
        source.registerCorsConfiguration(
                "/**",
                corsConfiguration)
        return source
    }

    @Bean
    @ConditionalOnMissingBean(HttpSessionManager::class)
    override fun httpSessionManager(): HttpSessionManager {
        return HttpSessionManager()
    }
}