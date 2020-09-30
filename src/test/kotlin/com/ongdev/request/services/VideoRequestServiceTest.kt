package com.ongdev.request.services

import com.ongdev.request.exceptions.VideoRequestCreationException
import com.ongdev.request.exceptions.VideoRequestDeleteFailedException
import com.ongdev.request.exceptions.VideoRequestNotFoundException
import com.ongdev.request.exceptions.VideoRequestUpdateFailedException
import com.ongdev.request.models.VideoRequest
import com.ongdev.request.models.dtos.VideoRequestTO
import com.ongdev.request.models.mappers.toVideoRequest
import com.ongdev.request.repositories.VideoRequestRepository
import com.ongdev.request.services.impls.VideoRequestServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
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
    fun createVideoRequest_shouldReturnCreatedVideoRequestTO() {
        Mockito.`when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenReturn(mockVideoRequest)

        val result = videoRequestService.createVideoRequest(mockVideoRequestTO)

        assertThat(result.id).isEqualTo(mockVideoRequest.id)
    }

    @Test
    fun createVideoRequest_shouldThrowErrorWhenVideoRequestTOIsNull() {
        Mockito.`when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenThrow()

        assertThrows<VideoRequestCreationException> { videoRequestService.createVideoRequest(mockVideoRequestTO) }
    }

    @Test
    fun editVideoRequest_shouldReturnCorrectUpdatedVideoRequest() {
        Mockito.`when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        Mockito.`when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenReturn(mockVideoRequest)

        val result = videoRequestService.editVideoRequest(mockVideoRequestTO, UUID.randomUUID().toString())

        assertThat(result.id).isEqualTo(mockVideoRequest.id)
    }

    @Test
    fun editVideoRequest_shouldThrowWhenFailedToFindEntity() {
        Mockito.`when`(videoRequestRepository.findById(any(UUID::class.java))).thenThrow()

        assertThrows<VideoRequestNotFoundException> { videoRequestService.editVideoRequest(mockVideoRequestTO, anyString()) }
    }

    @Test
    fun editVideoRequest_shouldThrowWhenFailedToSaveEntity() {
        Mockito.`when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        Mockito.`when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenThrow()

        assertThrows<VideoRequestUpdateFailedException> { videoRequestService.editVideoRequest(mockVideoRequestTO, anyString()) }
    }

    @Test
    fun deleteVideoRequest_shouldReturnTrue() {
        Mockito.doNothing().`when`(videoRequestRepository.deleteById(any(UUID::class.java)))

        val result = videoRequestService.deleteVideoRequest(anyString())
        assertThat(result).isTrue()
    }

    @Test
    fun deleteVideoRequest_shouldThrowNotFoundException() {
        Mockito.doThrow(VideoRequestNotFoundException::class.java).`when`(videoRequestRepository.deleteById(any(UUID::class.java)))

        assertThrows<VideoRequestNotFoundException> { videoRequestService.deleteVideoRequest(anyString()) }
    }

    @Test
    fun deleteVideoRequest_shouldReturnFalseWhenCannotDelete() {
        Mockito.doThrow(VideoRequestDeleteFailedException::class.java).`when`(videoRequestRepository.deleteById(any(UUID::class.java)))

        val result = videoRequestService.deleteVideoRequest(anyString())
        assertThat(result).isFalse()
    }

    @Test
    fun changeActivation_shouldReturnTrue() {
        Mockito.`when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        Mockito.`when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenReturn(mockVideoRequest)

        val result = videoRequestService.changeActivation(anyString())
        assertThat(result).isTrue()
    }

    @Test
    fun changeActivation_shouldThrowNotFoundException() {
        Mockito.`when`(videoRequestRepository.findById(any(UUID::class.java))).thenThrow()

        assertThrows<VideoRequestNotFoundException> { videoRequestService.changeActivation(anyString()) }
    }

    @Test
    fun changeActivation_shouldReturnFalse() {
        Mockito.`when`(videoRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalVideoRequest)
        Mockito.`when`(videoRequestRepository.save(any(VideoRequest::class.java))).thenThrow()

        val result = videoRequestService.changeActivation(anyString())
        assertThat(result).isFalse()
    }
}