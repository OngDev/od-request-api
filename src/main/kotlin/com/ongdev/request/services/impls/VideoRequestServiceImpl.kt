package com.ongdev.request.services.impls

import com.ongdev.request.exceptions.VideoRequestCreationException
import com.ongdev.request.exceptions.VideoRequestDeleteFailedException
import com.ongdev.request.exceptions.VideoRequestNotFoundException
import com.ongdev.request.exceptions.VideoRequestUpdateFailedException
import com.ongdev.request.models.VideoRequest
import com.ongdev.request.models.dtos.VideoRequestTO
import com.ongdev.request.models.mappers.toVideoRequest
import com.ongdev.request.models.mappers.toVideoRequestTO
import com.ongdev.request.models.mappers.toVideoRequestTOPage
import com.ongdev.request.repositories.VideoRequestRepository
import com.ongdev.request.services.VideoRequestService
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.*

@Service
class VideoRequestServiceImpl(private val videoRequestRepository: VideoRequestRepository) : VideoRequestService {
    override fun getVideoRequests(pageable: Pageable): Page<VideoRequestTO> {
        val videoRequestPage: Page<VideoRequest> = videoRequestRepository.findAll(pageable)
        return videoRequestPage.toVideoRequestTOPage()
    }

    override fun createVideoRequest(videoRequestTO: VideoRequestTO): VideoRequestTO {
        try {
            return videoRequestRepository.save(videoRequestTO.toVideoRequest()).toVideoRequestTO()
        } catch (ex: Exception) {
            throw VideoRequestCreationException(ex)
        }
    }

    override fun editVideoRequest(videoRequestTO: VideoRequestTO, requestId: String): VideoRequestTO {
        var videoRequest = videoRequestRepository.findById(UUID.fromString(requestId)).orElseThrow {VideoRequestNotFoundException()}
        videoRequest = videoRequestTO.toVideoRequest(videoRequest)
        try {
            return videoRequestRepository.save(videoRequest).toVideoRequestTO()
        } catch (ex: Exception) {
            throw VideoRequestUpdateFailedException(ex)
        }
    }

    override fun deleteVideoRequest(requestId: String) {
        try {
            videoRequestRepository.deleteById(UUID.fromString(requestId))
        } catch (ex : IllegalArgumentException) {
            throw VideoRequestDeleteFailedException()
        }
    }

    override fun changeActivation(requestId: String): VideoRequestTO {
        val videoRequest = videoRequestRepository.findById(UUID.fromString(requestId)).orElseThrow {VideoRequestNotFoundException()}
        try {
            videoRequest.isActive = ! videoRequest.isActive
            return videoRequestRepository.save(videoRequest).toVideoRequestTO()
        } catch (ex: Exception) {
            throw VideoRequestUpdateFailedException(ex)
        }
    }
}