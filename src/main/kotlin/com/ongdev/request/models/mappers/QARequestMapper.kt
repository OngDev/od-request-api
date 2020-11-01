package com.ongdev.request.models.mappers

import com.ongdev.request.models.QARequest
import com.ongdev.request.models.dtos.QARequestTO
import org.springframework.data.domain.Page
import java.util.*

fun QARequestTO.toQARequest() : QARequest {
    val qaRequest = QARequest()
    if(id != null) qaRequest.id = UUID.fromString(id)
    qaRequest.title = title
    qaRequest.description = description
    qaRequest.isActive = isActive
    qaRequest.isArchived = isArchived
    qaRequest.email = email
    qaRequest.votes = if (votes != null) (votes!!.map { vote -> vote.toVote()
    }).toMutableList() else mutableListOf()
    return qaRequest
}

fun QARequestTO.toQARequest(qaRequest: QARequest) : QARequest{
    qaRequest.title = title
    qaRequest.description = description
    return qaRequest
}

fun QARequest.toQARequestTO() = QARequestTO(
        id.toString(),
        title,
        description,
        isActive,
        isArchived,
        email,
        votes = votes.map {
            vote -> vote.toVoteTO()
        }
)

fun Page<QARequest>.toQARequestTOPage() : Page<QARequestTO>
        = this.map { qaRequest: QARequest -> qaRequest.toQARequestTO() }