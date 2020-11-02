package com.ongdev.request.services

import com.ongdev.request.models.QARequest
import com.ongdev.request.models.dtos.QARequestTO

interface QARequestService : RequestService<QARequestTO> {
    fun findByIdEditableRequestByUser(requestId: String, email: String) : QARequest
    fun findById(requestId: String): QARequest
}