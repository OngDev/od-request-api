package com.ongdev.request.models.mappers

import com.ongdev.request.models.UdemyRequest
import com.ongdev.request.models.dtos.udemy.UdemyRequestCreationTO
import com.ongdev.request.models.dtos.udemy.UdemyRequestTO
import com.ongdev.request.models.dtos.udemy.UdemyRequestUpdatingTO
import org.springframework.data.domain.Page

fun UdemyRequestCreationTO.toUdemyRequest() : UdemyRequest = UdemyRequest(title, description, url)

fun UdemyRequestUpdatingTO.toUdemyRequest(udemyRequest: UdemyRequest) : UdemyRequest {
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