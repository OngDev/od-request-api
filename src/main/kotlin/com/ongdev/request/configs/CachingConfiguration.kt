package com.ongdev.request.configs

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableCaching
class CachingConfiguration {
    @Bean
    fun cacheManager() : CacheManager {
        return ConcurrentMapCacheManager("videos", "udemy", "qna", "my-videos", "my-udemy", "my-qna");
    }
}