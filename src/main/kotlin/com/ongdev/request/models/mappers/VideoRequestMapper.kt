package com.ongdev.request.models.mappers

import com.ongdev.request.models.VideoRequest
import com.ongdev.request.models.dtos.VideoRequestTO
import org.springframework.data.domain.Page
import java.util.*

fun VideoRequestTO.toVideoRequest() : VideoRequest {
    val videoRequest = VideoRequest()
    if(id != null) videoRequest.id = UUID.fromString(id)
    videoRequest.title = title
    videoRequest.description = description
    return videoRequest
}

fun VideoRequest.toVideoRequestTO() = VideoRequestTO(
        id.toString(),
        title,
        description
//TODO: Add more information
)

fun Page<VideoRequest>.toVideoRequestTOPage() : Page<VideoRequestTO>
        = this.map { videoRequest: VideoRequest -> videoRequest.toVideoRequestTO() }