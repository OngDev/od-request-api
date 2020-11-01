package com.ongdev.request.services

import com.ongdev.request.models.VideoRequest
import com.ongdev.request.models.dtos.VideoRequestTO

interface VideoRequestService : RequestService<VideoRequestTO> {
    fun getRequestById(requestId: String) : VideoRequest
}