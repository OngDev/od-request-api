package com.ongdev.request.services

import com.ongdev.request.models.VideoRequest
import com.ongdev.request.models.dtos.VideoRequestTO

interface VideoRequestService : RequestService<VideoRequestTO> {
    fun findByIdEditableRequestByUser(requestId: String, email: String) : VideoRequest
    fun findById(requestId: String): VideoRequest
}