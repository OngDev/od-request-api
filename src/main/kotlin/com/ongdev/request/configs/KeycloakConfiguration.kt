package com.ongdev.request.configs

import org.keycloak.adapters.KeycloakConfigResolver
import org.keycloak.adapters.KeycloakDeployment
import org.keycloak.adapters.KeycloakDeploymentBuilder
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.representations.adapters.config.AdapterConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class KeycloakConfiguration {
    @Bean
    fun keycloakSpringBootConfigResolver()
            : KeycloakSpringBootConfigResolver = KeycloakSpringBootConfigResolver()

    @Bean
    fun keycloakDeployment(adapterConfig: AdapterConfig?): KeycloakDeployment? {
        return KeycloakDeploymentBuilder.build(adapterConfig)
    }

    @Bean
    fun keycloakConfigResolver(keycloakDeployment: KeycloakDeployment?): KeycloakConfigResolver? {
        return KeycloakConfigResolver { keycloakDeployment }
    }
}