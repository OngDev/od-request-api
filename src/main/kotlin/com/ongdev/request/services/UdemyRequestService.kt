package com.ongdev.request.services

import com.ongdev.request.models.UdemyRequest
import com.ongdev.request.models.dtos.UdemyRequestTO

interface UdemyRequestService : RequestService<UdemyRequestTO> {
    fun findByIdEditableRequestByUser(requestId: String, email: String) : UdemyRequest
    fun findById(requestId: String): UdemyRequest
}