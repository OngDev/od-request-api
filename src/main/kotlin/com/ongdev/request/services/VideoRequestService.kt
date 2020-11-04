package com.ongdev.request.services

import com.ongdev.request.models.VideoRequest
import com.ongdev.request.models.dtos.video.VideoRequestCreationTO
import com.ongdev.request.models.dtos.video.VideoRequestTO
import com.ongdev.request.models.dtos.video.VideoRequestUpdatingTO

interface VideoRequestService : RequestService<VideoRequestTO, VideoRequestCreationTO, VideoRequestUpdatingTO> {
    fun findByIdEditableRequestByUser(requestId: String, email: String) : VideoRequest
    fun findById(requestId: String): VideoRequest
}