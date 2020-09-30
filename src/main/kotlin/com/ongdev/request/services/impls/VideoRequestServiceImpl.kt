package com.ongdev.request.services.impls

import com.ongdev.request.models.dtos.VideoRequestTO
import com.ongdev.request.repositories.VideoRequestRepository
import com.ongdev.request.services.VideoRequestService
import org.springframework.stereotype.Service

@Service
class VideoRequestServiceImpl(videoRequestRepository: VideoRequestRepository) : VideoRequestService {
    override fun createVideoRequest(videoRequestTO: VideoRequestTO): VideoRequestTO {
        TODO("Not yet implemented")
    }

    override fun editVideoRequest(videoRequestTO: VideoRequestTO, requestId: String): VideoRequestTO {
        TODO("Not yet implemented")
    }

    override fun deleteVideoRequest(requestId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun changeActivation(requestId: String): Boolean {
        TODO("Not yet implemented")
    }
}