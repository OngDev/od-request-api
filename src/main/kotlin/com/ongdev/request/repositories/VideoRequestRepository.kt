package com.ongdev.request.repositories

import com.ongdev.request.models.VideoRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VideoRequestRepository : PagingAndSortingRepository<VideoRequest, UUID> {
    fun findAllByEmail(email: String?, pageable: Pageable?): Page<VideoRequest>
}