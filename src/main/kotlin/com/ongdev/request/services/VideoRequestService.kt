package com.ongdev.request.services

import com.ongdev.request.models.dtos.VideoRequestTO

interface VideoRequestService {
    fun createVideoRequest(videoRequestTO: VideoRequestTO) : VideoRequestTO
    fun editVideoRequest(videoRequestTO: VideoRequestTO, requestId: String): VideoRequestTO
    fun deleteVideoRequest(requestId: String): Boolean
    fun changeActivation(requestId: String): Boolean
}