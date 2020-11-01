package com.ongdev.request.services

import com.ongdev.request.models.QARequest
import com.ongdev.request.models.dtos.QARequestTO

interface QARequestService : RequestService<QARequestTO> {
    fun getRequestById(requestId: String) : QARequest
}