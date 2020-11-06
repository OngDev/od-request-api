package com.ongdev.request.repositories

import com.ongdev.request.models.QARequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface QARequestRepository : PagingAndSortingRepository<QARequest, UUID> {
    fun findAllByEmail(email: String?, pageable: Pageable?) : Page<QARequest>
}