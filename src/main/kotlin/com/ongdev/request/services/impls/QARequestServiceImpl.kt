package com.ongdev.request.services.impls

import com.ongdev.request.exceptions.qa.QARequestCreationException
import com.ongdev.request.exceptions.qa.QARequestDeleteFailedException
import com.ongdev.request.exceptions.qa.QARequestNotFoundException
import com.ongdev.request.exceptions.qa.QARequestUpdateFailedException
import com.ongdev.request.models.QARequest
import com.ongdev.request.models.Vote
import com.ongdev.request.models.dtos.qna.QARequestCreationTO
import com.ongdev.request.models.dtos.qna.QARequestTO
import com.ongdev.request.models.dtos.qna.QARequestUpdatingTO
import com.ongdev.request.models.mappers.toQARequest
import com.ongdev.request.models.mappers.toQARequestTO
import com.ongdev.request.models.mappers.toQARequestTOPage
import com.ongdev.request.repositories.QARequestRepository
import com.ongdev.request.services.QARequestService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class QARequestServiceImpl(private val qaRequestRepository: QARequestRepository) : QARequestService {
    override fun findByIdEditableRequestByUser(requestId: String, email: String): QARequest {
        val request = findById(requestId)
        if(request.email != email){
            throw QARequestUpdateFailedException("This is not your request, dont try to trick me :D")
        }
        return request
    }

    override fun findById(requestId: String): QARequest = qaRequestRepository
            .findById(UUID.fromString(requestId))
            .orElseThrow { QARequestNotFoundException() }

    override fun getRequests(pageable: Pageable): Page<QARequestTO> {
        val requestPage: Page<QARequest> = qaRequestRepository.findAll(pageable)
        return requestPage.toQARequestTOPage()
    }

    override fun createRequest(requestTO: QARequestCreationTO, email: String): QARequestTO {
        try {
            return qaRequestRepository.save(requestTO.toQARequest()).toQARequestTO()
        } catch (ex: Exception) {
            throw QARequestCreationException(ex)
        }
    }

    override fun editRequest(requestTO: QARequestUpdatingTO, requestId: String, email: String): QARequestTO {
        var request = findByIdEditableRequestByUser(requestId, email)

        request = requestTO.toQARequest(request)
        try {
            return qaRequestRepository.save(request).toQARequestTO()
        } catch (ex: Exception) {
            throw QARequestUpdateFailedException(ex = ex)
        }
    }

    override fun deleteRequest(requestId: String, email: String) {
        val request = findByIdEditableRequestByUser(requestId, email)

        try {
            qaRequestRepository.delete(request)
        } catch (ex : Exception) {
            throw QARequestDeleteFailedException(ex = ex)
        }
    }

    override fun changeActivation(requestId: String, email: String): QARequestTO {
        val request = findByIdEditableRequestByUser(requestId, email)

        try {
            request.isActive = ! request.isActive
            return qaRequestRepository.save(request).toQARequestTO()
        } catch (ex: Exception) {
            throw QARequestUpdateFailedException(ex = ex)
        }
    }

    override fun archive(requestId: String, email: String) {
        val request = findById(requestId)
        try {
            request.isArchived = true
            qaRequestRepository.save(request).toQARequestTO()
        } catch (ex: Exception) {
            throw QARequestUpdateFailedException(ex = ex)
        }
    }

    override fun upVote(requestId: String, email: String) {
        val qaRequest = findById(requestId)
        val existedVote = qaRequest.votes.find { vote -> vote.email == email }
        if (existedVote != null ) {
            if (!existedVote.isUp){
                existedVote.isUp = true
                qaRequestRepository.save(qaRequest)
            }
        } else {
            val vote = Vote(null, email, true)
            qaRequest.votes.add(vote)
            qaRequestRepository.save(qaRequest)
        }
    }

    override fun downVote(requestId: String, email: String) {
        val qaRequest = findById(requestId)
        val existedVote = qaRequest.votes.find { vote -> vote.email == email }
        if (existedVote != null ) {
            if (!existedVote.isUp){
                existedVote.isUp = false
                qaRequestRepository.save(qaRequest)
            }
        } else {
            val vote = Vote(null, email, false)
            qaRequest.votes.add(vote)
            qaRequestRepository.save(qaRequest)
        }
    }
}