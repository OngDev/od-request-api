package com.ongdev.request.services.impls

import com.ongdev.request.exceptions.udemy.UdemyRequestCreationException
import com.ongdev.request.exceptions.udemy.UdemyRequestDeleteFailedException
import com.ongdev.request.exceptions.udemy.UdemyRequestNotFoundException
import com.ongdev.request.exceptions.udemy.UdemyRequestUpdateFailedException
import com.ongdev.request.models.UdemyRequest
import com.ongdev.request.models.Vote
import com.ongdev.request.models.dtos.qna.QARequestCreationTO
import com.ongdev.request.models.dtos.qna.QARequestUpdatingTO
import com.ongdev.request.models.dtos.udemy.UdemyRequestCreationTO
import com.ongdev.request.models.dtos.udemy.UdemyRequestTO
import com.ongdev.request.models.dtos.udemy.UdemyRequestUpdatingTO
import com.ongdev.request.models.mappers.*
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

    override fun createRequest(requestTO: UdemyRequestCreationTO, email: String): UdemyRequestTO {
        try {
            val udemyRequest = requestTO.toUdemyRequest()
            udemyRequest.email = email
            return udemyRequestRepository.save(udemyRequest).toUdemyRequestTO()
        } catch (ex: Exception) {
            throw UdemyRequestCreationException(ex)
        }
    }

    override fun editRequest(requestTO: UdemyRequestUpdatingTO, requestId: String, email: String): UdemyRequestTO {
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

    override fun archive(requestId: String, email: String) {
        val request = findById(requestId)
        try {
            request.isArchived = true
            udemyRequestRepository.save(request).toUdemyRequestTO()
        } catch (ex: Exception) {
            throw UdemyRequestUpdateFailedException(ex = ex)
        }
    }

    override fun upVote(requestId: String, email: String) {
        val udemyRequest = findById(requestId)
        val existedVote = udemyRequest.votes.find { vote -> vote.email == email }
        if (existedVote != null ) {
            if (!existedVote.isUp){
                existedVote.isUp = true
                udemyRequestRepository.save(udemyRequest)
            }
        } else {
            val vote = Vote(null, email, true)
            udemyRequest.votes.add(vote)
            udemyRequestRepository.save(udemyRequest)
        }
    }

    override fun downVote(requestId: String, email: String) {
        val udemyRequest = findById(requestId)
        val existedVote = udemyRequest.votes.find { vote -> vote.email == email }
        if (existedVote != null ) {
            if (existedVote.isUp){
                existedVote.isUp = false
                udemyRequestRepository.save(udemyRequest)
            }
        } else {
            val vote = Vote(null, email, false)
            udemyRequest.votes.add(vote)
            udemyRequestRepository.save(udemyRequest)
        }
    }
}