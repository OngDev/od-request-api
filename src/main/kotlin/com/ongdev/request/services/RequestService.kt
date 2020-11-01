package com.ongdev.request.services

import com.ongdev.request.models.dtos.RequestTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface RequestService<T : RequestTO> {
    fun getRequests(pageable: Pageable) : Page<T>
    fun createRequest(requestTO: T) : T
    fun editRequest(requestTO: T, requestId: String): T
    fun deleteRequest(requestId: String)
    fun changeActivation(requestId: String): T
    fun archive(requestId: String)
    fun upVote(requestId: String)
    fun downVote(requestId: String)
}