package com.ongdev.request.models.mappers

import com.ongdev.request.models.QARequest
import com.ongdev.request.models.dtos.qna.QARequestCreationTO
import com.ongdev.request.models.dtos.qna.QARequestTO
import com.ongdev.request.models.dtos.qna.QARequestUpdatingTO
import org.springframework.data.domain.Page
import java.util.*

fun QARequestCreationTO.toQARequest() : QARequest = QARequest(title, description)

fun QARequestUpdatingTO.toQARequest(qaRequest: QARequest) : QARequest{
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