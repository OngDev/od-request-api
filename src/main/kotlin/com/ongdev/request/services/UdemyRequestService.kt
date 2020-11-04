package com.ongdev.request.services

import com.ongdev.request.models.UdemyRequest
import com.ongdev.request.models.dtos.qna.QARequestCreationTO
import com.ongdev.request.models.dtos.qna.QARequestTO
import com.ongdev.request.models.dtos.qna.QARequestUpdatingTO
import com.ongdev.request.models.dtos.udemy.UdemyRequestCreationTO
import com.ongdev.request.models.dtos.udemy.UdemyRequestTO
import com.ongdev.request.models.dtos.udemy.UdemyRequestUpdatingTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize

interface UdemyRequestService : RequestService<UdemyRequestTO, UdemyRequestCreationTO, UdemyRequestUpdatingTO> {
    fun findByIdEditableRequestByUser(requestId: String, email: String) : UdemyRequest
    fun findById(requestId: String): UdemyRequest
}