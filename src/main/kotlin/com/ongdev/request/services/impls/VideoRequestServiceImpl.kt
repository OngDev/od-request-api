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
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class VideoRequestServiceImpl(private val videoRequestRepository: VideoRequestRepository) : VideoRequestService {
    override fun findByIdEditableRequestByUser(requestId: String, email: String): VideoRequest {
        val request = findById(requestId)
        if(request.email != email){
            throw VideoRequestUpdateFailedException("This is not your request, dont try to trick me :D")
        }
        return request
    }

    override fun findById(requestId: String): VideoRequest = videoRequestRepository
            .findById(UUID.fromString(requestId))
            .orElseThrow { VideoRequestNotFoundException() }

    override fun getRequests(pageable: Pageable): Page<VideoRequestTO> {
        val videoRequestPage: Page<VideoRequest> = videoRequestRepository.findAll(pageable)
        return videoRequestPage.toVideoRequestTOPage()
    }

    override fun createRequest(requestTO: VideoRequestTO): VideoRequestTO {
        try {
            return videoRequestRepository.save(requestTO.toVideoRequest()).toVideoRequestTO()
        } catch (ex: Exception) {
            throw VideoRequestCreationException(ex)
        }
    }

    override fun editRequest(requestTO: VideoRequestTO, requestId: String, email: String): VideoRequestTO {
        var videoRequest = findByIdEditableRequestByUser(requestId, email)
        videoRequest = requestTO.toVideoRequest(videoRequest)
        try {
            return videoRequestRepository.save(videoRequest).toVideoRequestTO()
        } catch (ex: Exception) {
            throw VideoRequestUpdateFailedException(ex = ex)
        }
    }

    override fun deleteRequest(requestId: String, email: String) {
        val request = findByIdEditableRequestByUser(requestId, email)

        try {
            videoRequestRepository.delete(request)
        } catch (ex : IllegalArgumentException) {
            throw VideoRequestDeleteFailedException(ex)
        }
    }

    override fun changeActivation(requestId: String, email: String): VideoRequestTO {
        val videoRequest = findByIdEditableRequestByUser(requestId, email)
        try {
            videoRequest.isActive = !videoRequest.isActive
            return videoRequestRepository.save(videoRequest).toVideoRequestTO()
        } catch (ex: Exception) {
            throw VideoRequestUpdateFailedException(ex = ex)
        }
    }

    override fun archive(requestId: String) {
        val videoRequest = findById(requestId)
        try {
            videoRequest.isArchived = true
            videoRequestRepository.save(videoRequest).toVideoRequestTO()
        } catch (ex: Exception) {
            throw VideoRequestUpdateFailedException(ex = ex)
        }
    }

    override fun upVote(requestId: String, email: String) {
        TODO("Not yet implemented")
    }

    override fun downVote(requestId: String, email: String) {
        TODO("Not yet implemented")
    }
}