//package com.ongdev.request.uts.controllers
//
//import com.ongdev.request.models.dtos.video.VideoRequestTO
//import com.ongdev.request.services.VideoRequestService
//import com.ongdev.request.utils.JsonUtil.Companion.asJsonString
//import org.junit.jupiter.api.Test
//import com.nhaarman.mockitokotlin2.any
//import com.ongdev.request.configs.SecurityConfiguration
//import com.ongdev.request.controllers.VideoRequestController
//import com.ongdev.request.models.auth.UserInfo
//import com.ongdev.request.models.dtos.udemy.UdemyRequestTO
//import com.ongdev.request.models.dtos.video.VideoRequestCreationTO
//import com.ongdev.request.services.UserService
//import org.junit.jupiter.api.BeforeEach
//import org.mockito.ArgumentMatchers
//import org.mockito.ArgumentMatchers.anyString
//import org.mockito.Mockito
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
//import org.springframework.boot.test.mock.mockito.MockBean
//import org.springframework.data.domain.PageImpl
//import org.springframework.http.MediaType
//import org.springframework.security.test.context.support.WithMockUser
//import org.springframework.test.context.ContextConfiguration
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//import java.util.*
//
//@AutoConfigureMockMvc
//@ContextConfiguration(classes = [VideoRequestController::class, VideoRequestService::class, SecurityConfiguration::class, UserService::class])
//@WebMvcTest
//class VideoRequestControllerTest(@Autowired val mockMvc: MockMvc) {
//    @MockBean
//    private lateinit var videoRequestService: VideoRequestService
//
//    @MockBean
//    private lateinit var userService: UserService
//
//    private val testEmail: String = "test@ongdev.com"
//
//    private lateinit var mockVideoRequestTO: VideoRequestTO
//
//    @BeforeEach
//    internal fun setUp() {
//        mockVideoRequestTO = VideoRequestTO(
//                id = UUID.randomUUID().toString(),
//                title = "Test title",
//                description = "Test description",
//                email = testEmail,
//                isActive = true,
//                isArchived = false,
//                votes = listOf()
//        )
//
//        Mockito.`when`(userService.getUserInfoFromToken(any()))
//                .thenReturn(
//                        UserInfo(
//                                UUID.randomUUID().toString(),
//                                testEmail,
//                                "Ong Dev",
//                                true,
//                                setOf("USER")))
//    }
//
//    @Test
//    fun `Get video requests, should return correct response entity`() {
//        val mockVideoRequestTOPage = PageImpl<VideoRequestTO>(listOf(mockVideoRequestTO))
//        Mockito.`when`(videoRequestService.getRequests(any())).thenReturn(mockVideoRequestTOPage)
//
//        mockMvc.perform(get("/videos")
//                .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk)
//                .andExpect(jsonPath("content.[0].id").value(mockVideoRequestTO.id))
//                .andExpect(jsonPath("content.[0].title").value(mockVideoRequestTO.title))
//                .andExpect(jsonPath("content.[0].description").value(mockVideoRequestTO.description))
//    }
//
//    @WithMockUser(username = "test@ongdev.com", roles = ["USER"])
//    @Test
//    fun `Create video request, should return created video request`() {
//        Mockito.`when`(videoRequestService.createRequest(
//                any(),
//                anyString()))
//                .thenReturn(mockVideoRequestTO)
//        mockMvc.perform(post("/videos").accept(MediaType.APPLICATION_JSON_VALUE)
//                .content(asJsonString(VideoRequestCreationTO("Test title", "Test description")))
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .characterEncoding("utf-8"))
//                .andExpect(status().isOk)
//                .andDo(print())
//                .andExpect(jsonPath("\$.id").value(mockVideoRequestTO.id))
//                .andExpect(jsonPath("\$.title").value(mockVideoRequestTO.title))
//                .andExpect(jsonPath("\$.description").value(mockVideoRequestTO.description))
//    }
//}