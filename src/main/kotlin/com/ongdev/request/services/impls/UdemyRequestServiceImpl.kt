package com.ongdev.request.services.impls

import com.ongdev.request.exceptions.udemy.UdemyRequestCreationException
import com.ongdev.request.exceptions.udemy.UdemyRequestDeleteFailedException
import com.ongdev.request.exceptions.udemy.UdemyRequestNotFoundException
import com.ongdev.request.exceptions.udemy.UdemyRequestUpdateFailedException
import com.ongdev.request.models.UdemyRequest
import com.ongdev.request.models.dtos.UdemyRequestTO
import com.ongdev.request.models.mappers.toUdemyRequest
import com.ongdev.request.models.mappers.toUdemyRequestTO
import com.ongdev.request.models.mappers.toUdemyRequestTOPage
import com.ongdev.request.repositories.UdemyRequestRepository
import com.ongdev.request.services.UdemyRequestService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class UdemyRequestServiceImpl(private val udemyRequestRepository: UdemyRequestRepository) : UdemyRequestService {
    override fun findByIdEditableRequestByUser(requestId: String, email: String): UdemyRequest {
        val request = findById(requestId)
        if(request.email != email){
            throw UdemyRequestUpdateFailedException("This is not your request, dont try to trick me :D")
        }
        return request
    }

    override fun findById(requestId: String): UdemyRequest = udemyRequestRepository
            .findById(UUID.fromString(requestId))
            .orElseThrow { UdemyRequestNotFoundException() }

    override fun getRequests(pageable: Pageable): Page<UdemyRequestTO> {
        val requestPage: Page<UdemyRequest> = udemyRequestRepository.findAll(pageable)
        return requestPage.toUdemyRequestTOPage()
    }

    override fun createRequest(requestTO: UdemyRequestTO): UdemyRequestTO {
        try {
            return udemyRequestRepository.save(requestTO.toUdemyRequest()).toUdemyRequestTO()
        } catch (ex: Exception) {
            throw UdemyRequestCreationException(ex)
        }
    }

    override fun editRequest(requestTO: UdemyRequestTO, requestId: String, email: String): UdemyRequestTO {
        var request = findByIdEditableRequestByUser(requestId, email)
        request = requestTO.toUdemyRequest(request)
        try {
            return udemyRequestRepository.save(request).toUdemyRequestTO()
        } catch (ex: Exception) {
            throw UdemyRequestUpdateFailedException(ex = ex)
        }
    }

    override fun deleteRequest(requestId: String, email: String) {
        val request = findByIdEditableRequestByUser(requestId, email)

        try {
            udemyRequestRepository.delete(request)
        } catch (ex : IllegalArgumentException) {
            throw UdemyRequestDeleteFailedException(ex)
        }
    }

    override fun changeActivation(requestId: String, email: String): UdemyRequestTO {
        val request = findByIdEditableRequestByUser(requestId, email)
        try {
            request.isActive = !request.isActive
            return udemyRequestRepository.save(request).toUdemyRequestTO()
        } catch (ex: Exception) {
            throw UdemyRequestUpdateFailedException(ex = ex)
        }
    }

    override fun archive(requestId: String) {
        val request = findById(requestId)
        try {
            request.isArchived = true
            udemyRequestRepository.save(request).toUdemyRequestTO()
        } catch (ex: Exception) {
            throw UdemyRequestUpdateFailedException(ex = ex)
        }
    }

    override fun upVote(requestId: String, email: String) {
        TODO("Not yet implemented")
    }

    override fun downVote(requestId: String, email: String) {
        TODO("Not yet implemented")
    }
}