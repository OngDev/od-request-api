package com.ongdev.request.services.impls

import com.ongdev.request.exceptions.qa.QARequestCreationException
import com.ongdev.request.exceptions.qa.QARequestDeleteFailedException
import com.ongdev.request.exceptions.qa.QARequestNotFoundException
import com.ongdev.request.exceptions.qa.QARequestUpdateFailedException
import com.ongdev.request.models.QARequest
import com.ongdev.request.models.dtos.QARequestTO
import com.ongdev.request.models.mappers.toQARequest
import com.ongdev.request.models.mappers.toQARequestTO
import com.ongdev.request.models.mappers.toQARequestTOPage
import com.ongdev.request.repositories.QARequestRepository
import com.ongdev.request.services.QARequestService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

class QARequestServiceImpl(private val qaRequestRepository: QARequestRepository) : QARequestService {
    override fun getRequestById(requestId: String): QARequest = qaRequestRepository
            .findById(UUID.fromString(requestId))
            .orElseThrow { QARequestNotFoundException() }

    override fun getRequests(pageable: Pageable): Page<QARequestTO> {
        val requestPage: Page<QARequest> = qaRequestRepository.findAll(pageable)
        return requestPage.toQARequestTOPage()
    }

    override fun createRequest(requestTO: QARequestTO): QARequestTO {
        try {
            return qaRequestRepository.save(requestTO.toQARequest()).toQARequestTO()
        } catch (ex: Exception) {
            throw QARequestCreationException(ex)
        }
    }

    override fun editRequest(requestTO: QARequestTO, requestId: String): QARequestTO {
        var request = getRequestById(requestId)
        request = requestTO.toQARequest(request)
        try {
            return qaRequestRepository.save(request).toQARequestTO()
        } catch (ex: Exception) {
            throw QARequestUpdateFailedException(ex)
        }
    }

    override fun deleteRequest(requestId: String) {
        try {
            qaRequestRepository.deleteById(UUID.fromString(requestId))
        } catch (ex : IllegalArgumentException) {
            throw QARequestDeleteFailedException()
        }
    }

    override fun changeActivation(requestId: String): QARequestTO {
        val request = qaRequestRepository.findById(UUID.fromString(requestId)).orElseThrow {QARequestNotFoundException()}
        try {
            request.isActive = ! request.isActive
            return qaRequestRepository.save(request).toQARequestTO()
        } catch (ex: Exception) {
            throw QARequestUpdateFailedException(ex)
        }
    }

    override fun archive(requestId: String) {
        val request = qaRequestRepository.findById(UUID.fromString(requestId)).orElseThrow { QARequestNotFoundException()}
        try {
            request.isArchived = true
            qaRequestRepository.save(request).toQARequestTO()
        } catch (ex: Exception) {
            throw QARequestUpdateFailedException(ex)
        }
    }

    override fun upVote(requestId: String) {
        TODO("Not yet implemented")
    }

    override fun downVote(requestId: String) {
        TODO("Not yet implemented")
    }
}