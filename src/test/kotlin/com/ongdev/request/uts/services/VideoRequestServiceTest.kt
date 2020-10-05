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
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import java.util.*

internal class VideoRequestServiceTest {
    private val videoRequestRepository = Mockito.mock(VideoRequestRepository::class.java)

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
    fun `Create video request, should return correct videoRequestTO`() {
        Mockito.`when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenReturn(mockVideoRequest)

        val result = videoRequestService.createVideoRequest(mockVideoRequestTO)

        assertThat(result.id).isEqualTo(mockVideoRequest.id)
    }

    @Test
    fun `Create video request, should throw error when videoRequestTo is null`() {
        Mockito.`when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenThrow()

        assertThrows<VideoRequestCreationException> { videoRequestService.createVideoRequest(mockVideoRequestTO) }
    }

    @Test
    fun `Edit video request, should return updated videoRequestTo`() {
        Mockito.`when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        Mockito.`when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenReturn(mockVideoRequest)

        val result = videoRequestService.editVideoRequest(mockVideoRequestTO, UUID.randomUUID().toString())

        assertThat(result.id).isEqualTo(mockVideoRequest.id)
    }

    @Test
    fun `Edit video request, should throw error when failed to find entity`() {
        Mockito.`when`(videoRequestRepository.findById(any(UUID::class.java))).thenThrow()

        assertThrows<VideoRequestNotFoundException> { videoRequestService.editVideoRequest(mockVideoRequestTO, anyString()) }
    }

    @Test
    fun `Edit video request, should throw error when failed to save entity`() {
        Mockito.`when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        Mockito.`when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenThrow()

        assertThrows<VideoRequestUpdateFailedException> { videoRequestService.editVideoRequest(mockVideoRequestTO, anyString()) }
    }

    @Test
    fun `Delete video request, should return true`() {
        Mockito.doNothing().`when`(videoRequestRepository.deleteById(any(UUID::class.java)))

        val result = videoRequestService.deleteVideoRequest(anyString())
        assertThat(result).isTrue()
    }

    @Test
    fun `Delete video request, should throw not found exception`() {
        Mockito.doThrow(VideoRequestNotFoundException::class.java).`when`(videoRequestRepository.deleteById(any(UUID::class.java)))

        assertThrows<VideoRequestNotFoundException> { videoRequestService.deleteVideoRequest(anyString()) }
    }

    @Test
    fun `Delete video request, should return false when cannot delete`() {
        Mockito.doThrow(VideoRequestDeleteFailedException::class.java).`when`(videoRequestRepository.deleteById(any(UUID::class.java)))

        val result = videoRequestService.deleteVideoRequest(anyString())
        assertThat(result).isFalse()
    }

    @Test
    fun `Change activation, should return true`() {
        Mockito.`when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        Mockito.`when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenReturn(mockVideoRequest)

        val result = videoRequestService.changeActivation(anyString())
        assertThat(result).isTrue()
    }

    @Test
    fun `Change activation, should throw not found exception`() {
        Mockito.`when`(videoRequestRepository.findById(any(UUID::class.java))).thenThrow()

        assertThrows<VideoRequestNotFoundException> { videoRequestService.changeActivation(anyString()) }
    }

    @Test
    fun `Change activation, should return false when cannot save`() {
        Mockito.`when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        Mockito.`when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenThrow()

        val result = videoRequestService.changeActivation(anyString())
        assertThat(result).isFalse()
    }
}