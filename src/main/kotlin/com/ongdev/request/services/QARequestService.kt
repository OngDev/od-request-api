package com.ongdev.request.services

import com.ongdev.request.models.QARequest
import com.ongdev.request.models.dtos.qna.QARequestCreationTO
import com.ongdev.request.models.dtos.qna.QARequestTO
import com.ongdev.request.models.dtos.qna.QARequestUpdatingTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize

interface QARequestService : RequestService<QARequestTO, QARequestCreationTO, QARequestUpdatingTO>{
    fun findByIdEditableRequestByUser(requestId: String, email: String) : QARequest
    fun findById(requestId: String): QARequest
}