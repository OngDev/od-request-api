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
import org.mockito.Mockito.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.*

internal class VideoRequestServiceTest {
    private val videoRequestRepository = mock(VideoRequestRepository::class.java)

    private val videoRequestService: VideoRequestService = VideoRequestServiceImpl(videoRequestRepository)

    private lateinit var mockVideoRequestTO: VideoRequestTO
    private lateinit var mockOptionalVideoRequest: Optional<VideoRequest>
    private lateinit var mockVideoRequest: VideoRequest
    private lateinit var testEmail: String

    @BeforeEach
    internal fun setUp() {
        mockVideoRequestTO = VideoRequestTO(id = null, title = "Test title", description = "Test description", email = "test@ongdev.com")

        mockVideoRequest = mockVideoRequestTO.toVideoRequest()
        mockVideoRequest.id = UUID.randomUUID()
        mockOptionalVideoRequest = Optional.of(mockVideoRequest)
        testEmail = "test@ongdev.com"
    }

    @Test
    fun `Get video requests, should return pagination list`() {
        val mockRequestPage = PageImpl<VideoRequest>(listOf(mockVideoRequest))
        `when`(videoRequestRepository.findAll(any(Pageable::class.java))).thenReturn(mockRequestPage)

        val videoRequestTO = mockVideoRequestTO
        videoRequestTO.id = mockVideoRequest.id.toString()
        val mockRequestTOPage = PageImpl<VideoRequestTO>(listOf(videoRequestTO))
        val resultPage = videoRequestService.getRequests(PageRequest.of(1, 1))

        assertThat(resultPage.size).isEqualTo(mockRequestTOPage.size)
        assertThat(resultPage.get().findFirst().get().id).isEqualTo(mockRequestTOPage.get().findFirst().get().id)
    }

    @Test
    fun `Create video request, should return correct videoRequestTO`() {
        `when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenReturn(mockVideoRequest)

        val result = videoRequestService.createRequest(mockVideoRequestTO)

        assertThat(result.id).isEqualTo(mockVideoRequest.id.toString())
    }

    @Test
    fun `Create video request, should throw error when videoRequestTo is null`() {
        `when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<VideoRequestCreationException> { videoRequestService.createRequest(mockVideoRequestTO) }
    }

    @Test
    fun `Edit video request, should return updated videoRequestTo`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        `when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenReturn(mockVideoRequest)

        val result = videoRequestService.editRequest(mockVideoRequestTO, UUID.randomUUID().toString(), testEmail)

        assertThat(result.id).isEqualTo(mockVideoRequest.id.toString())
    }

    @Test
    fun `Edit video request, should throw error when failed to find entity`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenThrow(VideoRequestNotFoundException())

        assertThrows<VideoRequestNotFoundException> { videoRequestService.editRequest(mockVideoRequestTO, UUID.randomUUID().toString(), testEmail) }
    }

    @Test
    fun `Edit video request, should throw error when failed to save entity`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        `when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<VideoRequestUpdateFailedException> { videoRequestService.editRequest(mockVideoRequestTO, UUID.randomUUID().toString(), testEmail) }
    }

    @Test
    fun `Delete video request, should return true`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        `when`(videoRequestRepository.delete(any(VideoRequest::class.java))).then {
            // Do nothing
        }

        assertDoesNotThrow { videoRequestService.deleteRequest(UUID.randomUUID().toString(), testEmail) }
    }

    @Test
    fun `Delete video request, should return false when cannot delete`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        `when`(videoRequestRepository.delete(any(VideoRequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<VideoRequestDeleteFailedException> { videoRequestService.deleteRequest(UUID.randomUUID().toString(), testEmail) }
    }

    @Test
    fun `Change activation, should return updated entity`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        `when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenReturn(mockVideoRequest)

        val result = videoRequestService.changeActivation(UUID.randomUUID().toString(), testEmail)
        assertThat(result.id).isEqualTo(mockVideoRequest.id.toString())
    }

    @Test
    fun `Change activation, should throw not found exception`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenThrow(VideoRequestNotFoundException())

        assertThrows<VideoRequestNotFoundException> { videoRequestService.changeActivation(UUID.randomUUID().toString(), testEmail) }
    }

    @Test
    fun `Change activation, should throw error when cannot save`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        `when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<VideoRequestUpdateFailedException> { videoRequestService.changeActivation(UUID.randomUUID().toString(), testEmail) }
    }

    @Test
    fun `Archive video request, should work without any exception`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        `when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenReturn(mockVideoRequest)


        assertDoesNotThrow { videoRequestService.archive(UUID.randomUUID().toString()) }
    }

    @Test
    fun `Archive video request, should throw not found exception`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenThrow(VideoRequestNotFoundException())

        assertThrows<VideoRequestNotFoundException> { videoRequestService.archive(UUID.randomUUID().toString()) }
    }

    @Test
    fun `Archive video request, should throw exception`() {
        `when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        `when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<VideoRequestUpdateFailedException> { videoRequestService.archive(UUID.randomUUID().toString()) }
    }
}