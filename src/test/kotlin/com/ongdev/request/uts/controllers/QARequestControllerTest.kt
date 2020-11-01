package com.ongdev.request.uts.controllers

import com.ongdev.request.models.dtos.QARequestTO
import com.ongdev.request.services.QARequestService
import com.ongdev.request.utils.JsonUtil.Companion.asJsonString
import org.junit.jupiter.api.Test
import com.nhaarman.mockitokotlin2.any
import com.ongdev.request.controllers.QARequestController
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@AutoConfigureMockMvc
@ContextConfiguration(classes = [QARequestController::class, QARequestService::class])
@WebMvcTest
class QARequestControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockBean
    private lateinit var qaRequestService: QARequestService

    @Test
    fun `Get video requests, should return correct response entity`() {
        val mockQARequestTO = QARequestTO(
                UUID.randomUUID().toString(),
                "Test title",
                "Test description"
        )
        val mockQARequestTOPage = PageImpl<QARequestTO>(listOf(mockQARequestTO))
        Mockito.`when`(qaRequestService.getRequests(any())).thenReturn(mockQARequestTOPage)

        mockMvc.perform(get("/qna")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("content.[0].id").value(mockQARequestTO.id!!))
                .andExpect(jsonPath("content.[0].title").value(mockQARequestTO.title))
                .andExpect(jsonPath("content.[0].description").value(mockQARequestTO.description))
    }

    @Test
    fun `Create video request, should return created video request`() {
        val mockQARequestTO = QARequestTO(
                UUID.randomUUID().toString(),
                "Test title",
                "Test description"
        )
        Mockito.`when`(qaRequestService.createRequest(
                any()))
                .thenReturn(mockQARequestTO)
        mockMvc.perform(post("/qna").accept(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(QARequestTO(null, "Test title", "Test description")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk)
                .andDo(print())
                .andExpect(jsonPath("\$.id").value(mockQARequestTO.id!!))
                .andExpect(jsonPath("\$.title").value(mockQARequestTO.title))
                .andExpect(jsonPath("\$.description").value(mockQARequestTO.description))
    }
}