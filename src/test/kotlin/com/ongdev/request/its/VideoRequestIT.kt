package com.ongdev.request.its

import com.ongdev.request.models.dtos.VideoRequestTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VideoRequestIT(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun `Get video requests, should receive requests with status 200`(){
        val response = restTemplate.getForEntity<Page<VideoRequestTO>>("/videos")
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        //
    }
}