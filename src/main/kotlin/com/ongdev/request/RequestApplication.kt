package com.ongdev.request

import com.ongdev.request.configs.AppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
class RequestApplication

fun main(args: Array<String>) {
    runApplication<RequestApplication>(*args)
}
