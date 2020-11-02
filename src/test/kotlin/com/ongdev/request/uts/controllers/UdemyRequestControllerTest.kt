package com.ongdev.request.uts.controllers

import com.nhaarman.mockitokotlin2.any
import com.ongdev.request.controllers.QARequestController
import com.ongdev.request.controllers.UdemyRequestController
import com.ongdev.request.models.dtos.UdemyRequestTO
import com.ongdev.request.services.QARequestService
import com.ongdev.request.services.UdemyRequestService
import com.ongdev.request.utils.JsonUtil.Companion.asJsonString
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@AutoConfigureMockMvc
@ContextConfiguration(classes = [UdemyRequestController::class, UdemyRequestService::class])
@WebMvcTest
class UdemyRequestControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockBean
    private lateinit var udemyRequestService: UdemyRequestService

    @Test
    fun `Get video requests, should return correct response entity`() {
        val mockUdemyRequestTO = UdemyRequestTO(
                UUID.randomUUID().toString(),
                "Test title",
                "Test description",
                url = "Test url"
        )
        val mockUdemyRequestTOPage = PageImpl<UdemyRequestTO>(listOf(mockUdemyRequestTO))
        Mockito.`when`(udemyRequestService.getRequests(any())).thenReturn(mockUdemyRequestTOPage)

        mockMvc.perform(get("/udemy")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("content.[0].id").value(mockUdemyRequestTO.id!!))
                .andExpect(jsonPath("content.[0].title").value(mockUdemyRequestTO.title))
                .andExpect(jsonPath("content.[0].description").value(mockUdemyRequestTO.description))
                .andExpect(jsonPath("content.[0].url").value(mockUdemyRequestTO.url))
    }

    @WithMockUser(username = "test@ongdev.com")
    @Test
    fun `Create video request, should return created video request`() {
        val mockUdemyRequestTO = UdemyRequestTO(
                UUID.randomUUID().toString(),
                "Test title",
                "Test description",
                url = "Test url"
        )
        Mockito.`when`(udemyRequestService.createRequest(
                any()))
                .thenReturn(mockUdemyRequestTO)
        mockMvc.perform(post("/udemy").accept(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(UdemyRequestTO(null, "Test title", "Test description", url = "Test url")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk)
                .andDo(print())
                .andExpect(jsonPath("\$.id").value(mockUdemyRequestTO.id!!))
                .andExpect(jsonPath("\$.title").value(mockUdemyRequestTO.title))
                .andExpect(jsonPath("\$.description").value(mockUdemyRequestTO.description))
                .andExpect(jsonPath("\$.url").value(mockUdemyRequestTO.url))
    }
}