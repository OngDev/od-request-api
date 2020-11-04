package com.ongdev.request.models.mappers

import com.ongdev.request.models.VideoRequest
import com.ongdev.request.models.dtos.video.VideoRequestCreationTO
import com.ongdev.request.models.dtos.video.VideoRequestTO
import com.ongdev.request.models.dtos.video.VideoRequestUpdatingTO
import org.springframework.data.domain.Page
import java.util.*

fun VideoRequestCreationTO.toVideoRequest() : VideoRequest = VideoRequest(title, description)

fun VideoRequestUpdatingTO.toVideoRequest(videoRequest: VideoRequest) : VideoRequest{
    videoRequest.title = title
    videoRequest.description = description
    return videoRequest
}

fun VideoRequest.toVideoRequestTO() = VideoRequestTO(
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

fun Page<VideoRequest>.toVideoRequestTOPage() : Page<VideoRequestTO>
        = this.map { videoRequest: VideoRequest -> videoRequest.toVideoRequestTO() }