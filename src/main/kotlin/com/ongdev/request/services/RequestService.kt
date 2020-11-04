package com.ongdev.request.services

import com.ongdev.request.models.dtos.CreationTO
import com.ongdev.request.models.dtos.RequestTO
import com.ongdev.request.models.dtos.UpdatingTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize

interface RequestService<T : RequestTO, U: CreationTO, V: UpdatingTO> {
    fun createRequest(requestTO: U, email: String): T

    fun editRequest(requestTO: V, requestId: String, email: String): T

    fun getRequests(pageable: Pageable) : Page<T>
    fun deleteRequest(requestId: String, email: String)
    fun changeActivation(requestId: String, email: String): T

    @PreAuthorize("hasAnyRole('ONGDEV')")
    fun archive(requestId: String, email: String)
    fun upVote(requestId: String, email: String)
    fun downVote(requestId: String, email: String)
}