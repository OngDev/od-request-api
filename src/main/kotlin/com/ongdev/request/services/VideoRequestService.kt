package com.ongdev.request.services

import com.ongdev.request.models.dtos.VideoRequestTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface VideoRequestService {
    fun getVideoRequests(pageable: Pageable) : Page<VideoRequestTO>
    fun createVideoRequest(videoRequestTO: VideoRequestTO) : VideoRequestTO
    fun editVideoRequest(videoRequestTO: VideoRequestTO, requestId: String): VideoRequestTO
    fun deleteVideoRequest(requestId: String)
    fun changeActivation(requestId: String): VideoRequestTO
}