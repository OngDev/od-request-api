package com.ongdev.request.uts.controllers

import com.ongdev.request.models.dtos.VideoRequestTO
import com.ongdev.request.services.VideoRequestService
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@WebMvcTest
class VideoRequestControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockBean
    private lateinit var videoRequestService: VideoRequestService

    @Test
    fun `Get video requests, should return correct response entity`() {
        // Ignore
    }

    @Test
    fun `Create video request, should return created video request`() {
        val mockVideoRequestTO = VideoRequestTO(
                UUID.randomUUID().toString(),
                "Test title",
                "Test description"
        )
        Mockito.`when`(videoRequestService.createVideoRequest(
                any(VideoRequestTO::class.java)))
                .thenReturn(mockVideoRequestTO)
        mockMvc.perform(get("/api/videos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$.id").value(mockVideoRequestTO.id!!))
                .andExpect(jsonPath("\$.title").value(mockVideoRequestTO.title))
                .andExpect(jsonPath("\$.description").value(mockVideoRequestTO.description))
    }
}