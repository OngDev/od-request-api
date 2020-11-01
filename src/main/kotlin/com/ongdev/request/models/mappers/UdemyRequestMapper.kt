package com.ongdev.request.models.mappers

import com.ongdev.request.models.UdemyRequest
import com.ongdev.request.models.dtos.UdemyRequestTO
import org.springframework.data.domain.Page
import java.util.*

fun UdemyRequestTO.toUdemyRequest() : UdemyRequest {
    val udemyRequest = UdemyRequest()
    if(id != null) udemyRequest.id = UUID.fromString(id)
    udemyRequest.title = title
    udemyRequest.description = description
    udemyRequest.url = url
    udemyRequest.isActive = isActive
    udemyRequest.isArchived = isArchived
    udemyRequest.email = email
    udemyRequest.votes = if (votes != null) (votes!!.map { vote -> vote.toVote()
    }).toMutableList() else mutableListOf()
    return udemyRequest
}

fun UdemyRequestTO.toUdemyRequest(udemyRequest: UdemyRequest) : UdemyRequest {
    udemyRequest.title = title
    udemyRequest.description = description
    udemyRequest.url = url
    return udemyRequest
}

fun UdemyRequest.toUdemyRequestTO() = UdemyRequestTO(
        id.toString(),
        title,
        description,
        isActive,
        isArchived,
        email,
        url,
        votes = votes.map {
            vote -> vote.toVoteTO()
        }
)

fun Page<UdemyRequest>.toUdemyRequestTOPage() : Page<UdemyRequestTO>
        = this.map { udemyRequest: UdemyRequest -> udemyRequest.toUdemyRequestTO() }