package com.ongdev.request.uts.services

import com.ongdev.request.exceptions.VideoRequestCreationException
import com.ongdev.request.exceptions.VideoRequestDeleteFailedException
import com.ongdev.request.exceptions.VideoRequestNotFoundException
import com.ongdev.request.exceptions.VideoRequestUpdateFailedException
import com.ongdev.request.models.VideoRequest
import com.ongdev.request.models.dtos.VideoRequestTO
import com.ongdev.request.models.mappers.toVideoRequest
import com.ongdev.request.repositories.VideoRequestRepository
import com.ongdev.request.services.VideoRequestService
import com.ongdev.request.services.impls.VideoRequestServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.*

internal class VideoRequestServiceTest {
    private val videoRequestRepository = mock(VideoRequestRepository::class.java)

    private val videoRequestService: VideoRequestService = VideoRequestServiceImpl(videoRequestRepository)

    private lateinit var mockVideoRequestTO : VideoRequestTO
    private lateinit var mockOptionalVideoRequest: Optional<VideoRequest>
    private lateinit var mockVideoRequest : VideoRequest

    @BeforeEach
    internal fun setUp() {
        mockVideoRequestTO = VideoRequestTO(title = "Test title", description = "Test description")

        mockVideoRequest = mockVideoRequestTO.toVideoRequest()
        mockVideoRequest.id = UUID.randomUUID()
        mockOptionalVideoRequest = Optional.of(mockVideoRequest)
    }

    @Test
    fun `Get video requests, should return pagination list`() {
        val mockRequestPage = PageImpl<VideoRequest>(listOf(mockVideoRequest))
        `when`(videoRequestRepository.findAll(any(Pageable::class.java))).thenReturn(mockRequestPage)

        val videoRequestTO = mockVideoRequestTO
        videoRequestTO.id = mockVideoRequest.id.toString()
        val mockRequestTOPage = PageImpl<VideoRequestTO>(listOf(videoRequestTO))
        val resultPage = videoRequestService.getVideoRequests(PageRequest.of(1,1))

        assertThat(resultPage.size).isEqualTo(mockRequestTOPage.size)
        assertThat(resultPage.get().findFirst().get().id).isEqualTo(mockRequestTOPage.get().findFirst().get().id)
    }

    @Test
    fun `Create video request, should return correct videoRequestTO`() {
        `when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenReturn(mockVideoRequest)

        val result = videoRequestService.createVideoRequest(mockVideoRequestTO)

        assertThat(result.id).isEqualTo(mockVideoRequest.id.toString())
    }

    @Test
    fun `Create video request, should throw error when videoRequestTo is null`() {
        `when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<VideoRequestCreationException> { videoRequestService.createVideoRequest(mockVideoRequestTO) }
    }

    @Test
    fun `Edit video request, should return updated videoRequestTo`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        `when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenReturn(mockVideoRequest)

        val result = videoRequestService.editVideoRequest(mockVideoRequestTO, UUID.randomUUID().toString())

        assertThat(result.id).isEqualTo(mockVideoRequest.id.toString())
    }

    @Test
    fun `Edit video request, should throw error when failed to find entity`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenThrow(VideoRequestNotFoundException())

        assertThrows<VideoRequestNotFoundException> { videoRequestService.editVideoRequest(mockVideoRequestTO, UUID.randomUUID().toString()) }
    }

    @Test
    fun `Edit video request, should throw error when failed to save entity`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        `when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<VideoRequestUpdateFailedException> { videoRequestService.editVideoRequest(mockVideoRequestTO, UUID.randomUUID().toString()) }
    }

    @Test
    fun `Delete video request, should return true`() {
        `when`(videoRequestRepository.deleteById(any(UUID::class.java))).then {
            // Do nothing
        }

        videoRequestService.deleteVideoRequest(UUID.randomUUID().toString())
        assertDoesNotThrow { VideoRequestDeleteFailedException() }
    }

    @Test
    fun `Delete video request, should return false when cannot delete`() {
        `when`(videoRequestRepository.deleteById(any(UUID::class.java))).thenThrow(IllegalArgumentException::class.java)

        assertThrows<VideoRequestDeleteFailedException> { videoRequestService.deleteVideoRequest("Test") }
    }

    @Test
    fun `Change activation, should return updated entity`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        `when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenReturn(mockVideoRequest)

        val result = videoRequestService.changeActivation(UUID.randomUUID().toString())
        assertThat(result.id).isEqualTo(mockVideoRequest.id.toString())
    }

    @Test
    fun `Change activation, should throw not found exception`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenThrow(VideoRequestNotFoundException())

        assertThrows<VideoRequestNotFoundException> { videoRequestService.changeActivation(UUID.randomUUID().toString()) }
    }

    @Test
    fun `Change activation, should throw error when cannot save`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        `when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<VideoRequestUpdateFailedException> { videoRequestService.changeActivation(UUID.randomUUID().toString()) }
    }
}