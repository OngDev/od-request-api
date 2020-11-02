package com.ongdev.request.services.impls

import com.ongdev.request.configs.AppProperties
import com.ongdev.request.exceptions.auth.UserNotFoundException
import com.ongdev.request.models.auth.UserInfo
import com.ongdev.request.services.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.util.StringUtils
import org.springframework.web.client.RestTemplate

@Service
class UserServiceImpl(
        val restTemplate: RestTemplate,
        val appProperties: AppProperties
) : UserService {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)
    }
    override fun getUserInfoFromToken(token: String?): UserInfo? {
        if (!StringUtils.hasText(token)) {
            return null
        }

        val headers: MultiValueMap<String, String> = LinkedMultiValueMap()
        headers.add("Authorization", "Bearer $token")
        headers.add("Content-Type", "application/json")

        logger.info("Fetching user info by token: $token")
        val request: HttpEntity<Void> = HttpEntity(headers)
        val responseEntity: ResponseEntity<UserInfo> = restTemplate.exchange(
                appProperties.authUrl + "/users/me",
                HttpMethod.GET,
                request,
                UserInfo::class.java)
        if (responseEntity.statusCode != HttpStatus.OK) {
            logger.info("User is not found by token")
            throw UserNotFoundException()
        }
        val userInfo: UserInfo = responseEntity.body!!
        logger.info("Fetched user with id: ${userInfo.id}")
        return userInfo
    }
}