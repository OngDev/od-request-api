package com.ongdev.request.uts.controllers

import com.ongdev.request.models.dtos.VideoRequestTO
import com.ongdev.request.services.VideoRequestService
import com.ongdev.request.utils.JsonUtil.Companion.asJsonString
import org.junit.jupiter.api.Test
import com.nhaarman.mockitokotlin2.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest
class VideoRequestControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockBean
    private lateinit var videoRequestService: VideoRequestService

    @Test
    fun `Get video requests, should return correct response entity`() {
        val mockVideoRequestTO = VideoRequestTO(
                UUID.randomUUID().toString(),
                "Test title",
                "Test description"
        )
        val mockVideoRequestTOPage = PageImpl<VideoRequestTO>(listOf(mockVideoRequestTO))
        Mockito.`when`(videoRequestService.getVideoRequests(PageRequest.of(0,10, Sort.Direction.DESC, "title"))).thenReturn(mockVideoRequestTOPage)

        mockMvc.perform(get("/videos")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("content.[0].id").value(mockVideoRequestTO.id!!))
                .andExpect(jsonPath("content.[0].title").value(mockVideoRequestTO.title))
                .andExpect(jsonPath("content.[0].description").value(mockVideoRequestTO.description))
    }

    @Test
    fun `Create video request, should return created video request`() {
        val mockVideoRequestTO = VideoRequestTO(
                UUID.randomUUID().toString(),
                "Test title",
                "Test description"
        )
        Mockito.`when`(videoRequestService.createVideoRequest(
                any()))
                .thenReturn(mockVideoRequestTO)
        mockMvc.perform(post("/videos").accept(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(VideoRequestTO(null, "Test title", "Test description")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk)
                .andDo(print())
                .andExpect(jsonPath("\$.id").value(mockVideoRequestTO.id!!))
                .andExpect(jsonPath("\$.title").value(mockVideoRequestTO.title))
                .andExpect(jsonPath("\$.description").value(mockVideoRequestTO.description))
    }
}