package com.ongdev.request.uts.services

import com.ongdev.request.exceptions.qa.QARequestCreationException
import com.ongdev.request.exceptions.qa.QARequestDeleteFailedException
import com.ongdev.request.exceptions.qa.QARequestNotFoundException
import com.ongdev.request.exceptions.qa.QARequestUpdateFailedException
import com.ongdev.request.models.QARequest
import com.ongdev.request.models.dtos.QARequestTO
import com.ongdev.request.models.mappers.toQARequest
import com.ongdev.request.repositories.QARequestRepository
import com.ongdev.request.services.QARequestService
import com.ongdev.request.services.impls.QARequestServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.*

internal class QARequestServiceTest {
    private val qaRequestRepository = mock(QARequestRepository::class.java)

    private val qaRequestService: QARequestService = QARequestServiceImpl(qaRequestRepository)

    private lateinit var mockQARequestTO: QARequestTO
    private lateinit var mockOptionalQARequest: Optional<QARequest>
    private lateinit var mockQARequest: QARequest

    @BeforeEach
    internal fun setUp() {
        mockQARequestTO = QARequestTO(id = null, title = "Test title", description = "Test description")

        mockQARequest = mockQARequestTO.toQARequest()
        mockQARequest.id = UUID.randomUUID()
        mockOptionalQARequest = Optional.of(mockQARequest)
    }

    @Test
    fun `Get video requests, should return pagination list`() {
        val mockRequestPage = PageImpl<QARequest>(listOf(mockQARequest))
        `when`(qaRequestRepository.findAll(any(Pageable::class.java))).thenReturn(mockRequestPage)

        val qaRequestTO = mockQARequestTO
        qaRequestTO.id = mockQARequest.id.toString()
        val mockRequestTOPage = PageImpl<QARequestTO>(listOf(qaRequestTO))
        val resultPage = qaRequestService.getRequests(PageRequest.of(1, 1))

        assertThat(resultPage.size).isEqualTo(mockRequestTOPage.size)
        assertThat(resultPage.get().findFirst().get().id).isEqualTo(mockRequestTOPage.get().findFirst().get().id)
    }

    @Test
    fun `Create video request, should return correct qaRequestTO`() {
        `when`(qaRequestRepository.save(any(QARequest::class.java))).thenReturn(mockQARequest)

        val result = qaRequestService.createRequest(mockQARequestTO)

        assertThat(result.id).isEqualTo(mockQARequest.id.toString())
    }

    @Test
    fun `Create video request, should throw error when qaRequestTo is null`() {
        `when`(qaRequestRepository.save(any(QARequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<QARequestCreationException> { qaRequestService.createRequest(mockQARequestTO) }
    }

    @Test
    fun `Edit video request, should return updated qaRequestTo`() {
        `when`(qaRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalQARequest)
        `when`(qaRequestRepository.save(any(QARequest::class.java))).thenReturn(mockQARequest)

        val result = qaRequestService.editRequest(mockQARequestTO, UUID.randomUUID().toString())

        assertThat(result.id).isEqualTo(mockQARequest.id.toString())
    }

    @Test
    fun `Edit video request, should throw error when failed to find entity`() {
        `when`(qaRequestRepository.findById(any(UUID::class.java))).thenThrow(QARequestNotFoundException())

        assertThrows<QARequestNotFoundException> { qaRequestService.editRequest(mockQARequestTO, UUID.randomUUID().toString()) }
    }

    @Test
    fun `Edit video request, should throw error when failed to save entity`() {
        `when`(qaRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalQARequest)
        `when`(qaRequestRepository.save(any(QARequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<QARequestUpdateFailedException> { qaRequestService.editRequest(mockQARequestTO, UUID.randomUUID().toString()) }
    }

    @Test
    fun `Delete video request, should return true`() {
        `when`(qaRequestRepository.deleteById(any(UUID::class.java))).then {
            // Do nothing
        }

        qaRequestService.deleteRequest(UUID.randomUUID().toString())
        assertDoesNotThrow { QARequestDeleteFailedException() }
    }

    @Test
    fun `Delete video request, should return false when cannot delete`() {
        `when`(qaRequestRepository.deleteById(any(UUID::class.java))).thenThrow(IllegalArgumentException::class.java)

        assertThrows<QARequestDeleteFailedException> { qaRequestService.deleteRequest("Test") }
    }

    @Test
    fun `Change activation, should return updated entity`() {
        `when`(qaRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalQARequest)
        `when`(qaRequestRepository.save(any(QARequest::class.java))).thenReturn(mockQARequest)

        val result = qaRequestService.changeActivation(UUID.randomUUID().toString())
        assertThat(result.id).isEqualTo(mockQARequest.id.toString())
    }

    @Test
    fun `Change activation, should throw not found exception`() {
        `when`(qaRequestRepository.findById(any(UUID::class.java))).thenThrow(QARequestNotFoundException())

        assertThrows<QARequestNotFoundException> { qaRequestService.changeActivation(UUID.randomUUID().toString()) }
    }

    @Test
    fun `Change activation, should throw error when cannot save`() {
        `when`(qaRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalQARequest)
        `when`(qaRequestRepository.save(any(QARequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<QARequestUpdateFailedException> { qaRequestService.changeActivation(UUID.randomUUID().toString()) }
    }

    @Test
    fun `Archive video request, should work without any exception`() {
        `when`(qaRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalQARequest)
        `when`(qaRequestRepository.save(any(QARequest::class.java))).thenReturn(mockQARequest)


        assertDoesNotThrow { qaRequestService.archive(UUID.randomUUID().toString()) }
    }

    @Test
    fun `Archive video request, should throw not found exception`() {
        `when`(qaRequestRepository.findById(any(UUID::class.java))).thenThrow(QARequestNotFoundException())

        assertThrows<QARequestNotFoundException> { qaRequestService.archive(UUID.randomUUID().toString()) }
    }

    @Test
    fun `Archive video request, should throw exception`() {
        `when`(qaRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalQARequest)
        `when`(qaRequestRepository.save(any(QARequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<QARequestUpdateFailedException> { qaRequestService.archive(UUID.randomUUID().toString()) }
    }
}