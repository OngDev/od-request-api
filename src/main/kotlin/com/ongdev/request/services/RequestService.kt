package com.ongdev.request.services

import com.ongdev.request.models.dtos.RequestTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize

interface RequestService<T : RequestTO> {
    fun getRequests(pageable: Pageable) : Page<T>
    @PreAuthorize("hasAnyRole('USER', 'ONGDEV')")
    fun createRequest(requestTO: T) : T
    @PreAuthorize("hasAnyRole('USER', 'ONGDEV')")
    fun editRequest(requestTO: T, requestId: String, email: String): T

    @PreAuthorize("hasAnyRole('USER', 'ONGDEV')")
    fun deleteRequest(requestId: String, email: String)
    @PreAuthorize("hasAnyRole('USER', 'ONGDEV')")
    fun changeActivation(requestId: String, email: String): T

    @PreAuthorize("hasAnyRole('ONGDEV')")
    fun archive(requestId: String)
    @PreAuthorize("hasAnyRole('USER', 'ONGDEV')")
    fun upVote(requestId: String, email: String)
    @PreAuthorize("hasAnyRole('USER', 'ONGDEV')")
    fun downVote(requestId: String, email: String)
}