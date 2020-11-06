package com.ongdev.request.uts.services

import com.ongdev.request.exceptions.udemy.UdemyRequestCreationException
import com.ongdev.request.exceptions.udemy.UdemyRequestDeleteFailedException
import com.ongdev.request.exceptions.udemy.UdemyRequestNotFoundException
import com.ongdev.request.exceptions.udemy.UdemyRequestUpdateFailedException
import com.ongdev.request.models.UdemyRequest
import com.ongdev.request.models.dtos.udemy.UdemyRequestCreationTO
import com.ongdev.request.models.dtos.udemy.UdemyRequestTO
import com.ongdev.request.models.dtos.udemy.UdemyRequestUpdatingTO
import com.ongdev.request.models.mappers.toUdemyRequest
import com.ongdev.request.repositories.UdemyRequestRepository
import com.ongdev.request.services.UdemyRequestService
import com.ongdev.request.services.impls.UdemyRequestServiceImpl
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

internal class UdemyRequestServiceTest {
    private val udemyRequestRepository = mock(UdemyRequestRepository::class.java)

    private val udemyRequestService: UdemyRequestService = UdemyRequestServiceImpl(udemyRequestRepository)

    private lateinit var mockUdemyRequestTO: UdemyRequestTO
    private lateinit var mockUdemyRequestCreationTO: UdemyRequestCreationTO
    private lateinit var mockUdemyRequestUpdatingTO: UdemyRequestUpdatingTO
    private lateinit var mockOptionalUdemyRequest: Optional<UdemyRequest>
    private lateinit var mockUdemyRequest: UdemyRequest
    private lateinit var testEmail: String

    @BeforeEach
    internal fun setUp() {
        testEmail = "test@ongdev.com"
        mockUdemyRequestTO = UdemyRequestTO(
                id = UUID.randomUUID().toString(),
                title = "Test title",
                description = "Test description",
                url = "Test Url",
                email = testEmail,
                isActive = true,
                isArchived = false,
                votes = listOf()
        )

        mockUdemyRequestCreationTO = UdemyRequestCreationTO("Test title","Test description","Test Url")
        mockUdemyRequestUpdatingTO = UdemyRequestUpdatingTO("Test title","Test description","Test Url")
        mockUdemyRequest = mockUdemyRequestCreationTO.toUdemyRequest()
        mockUdemyRequest.email = testEmail
        mockUdemyRequest.id = UUID.randomUUID()
        mockOptionalUdemyRequest = Optional.of(mockUdemyRequest)
    }

    @Test
    fun `Get udemy requests, should return pagination list`() {
        val mockRequestPage = PageImpl<UdemyRequest>(listOf(mockUdemyRequest))
        `when`(udemyRequestRepository.findAll(any(Pageable::class.java))).thenReturn(mockRequestPage)

        val udemyRequestTO = mockUdemyRequestTO
        udemyRequestTO.id = mockUdemyRequest.id.toString()
        val mockRequestTOPage = PageImpl<UdemyRequestTO>(listOf(udemyRequestTO))
        val resultPage = udemyRequestService.getRequests(PageRequest.of(1, 1))

        assertThat(resultPage.size).isEqualTo(mockRequestTOPage.size)
        assertThat(resultPage.get().findFirst().get().id).isEqualTo(mockRequestTOPage.get().findFirst().get().id)
    }

    @Test
    fun `Get my udemy requests, should return pagination list`() {
        val mockRequestPage = PageImpl<UdemyRequest>(listOf(mockUdemyRequest))
        `when`(udemyRequestRepository.findAllByEmail(anyString(), any(Pageable::class.java))).thenReturn(mockRequestPage)

        val udemyRequestTO = mockUdemyRequestTO
        udemyRequestTO.id = mockUdemyRequest.id.toString()
        val mockRequestTOPage = PageImpl<UdemyRequestTO>(listOf(udemyRequestTO))
        val resultPage = udemyRequestService.getMyRequests(PageRequest.of(1, 1), testEmail)

        assertThat(resultPage.size).isEqualTo(mockRequestTOPage.size)
        assertThat(resultPage.get().findFirst().get().id).isEqualTo(mockRequestTOPage.get().findFirst().get().id)
    }

    @Test
    fun `Create udemy request, should return correct udemyRequestTO`() {
        `when`(udemyRequestRepository.save(any(UdemyRequest::class.java))).thenReturn(mockUdemyRequest)

        val result = udemyRequestService.createRequest(mockUdemyRequestCreationTO, testEmail)

        assertThat(result.id).isEqualTo(mockUdemyRequest.id.toString())
    }

    @Test
    fun `Create udemy request, should throw error when udemyRequestTo is null`() {
        `when`(udemyRequestRepository.save(any(UdemyRequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<UdemyRequestCreationException> { udemyRequestService.createRequest(mockUdemyRequestCreationTO, testEmail) }
    }

    @Test
    fun `Edit udemy request, should return updated udemyRequestTo`() {
        `when`(udemyRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalUdemyRequest)
        `when`(udemyRequestRepository.save(any(UdemyRequest::class.java))).thenReturn(mockUdemyRequest)

        val result = udemyRequestService.editRequest(mockUdemyRequestUpdatingTO, UUID.randomUUID().toString(), testEmail)

        assertThat(result.id).isEqualTo(mockUdemyRequest.id.toString())
    }

    @Test
    fun `Edit udemy request, should throw error when failed to find entity`() {
        `when`(udemyRequestRepository.findById(any(UUID::class.java))).thenThrow(UdemyRequestNotFoundException())

        assertThrows<UdemyRequestNotFoundException> { udemyRequestService.editRequest(mockUdemyRequestUpdatingTO, UUID.randomUUID().toString(), testEmail) }
    }

    @Test
    fun `Edit udemy request, should throw error when failed to save entity`() {
        `when`(udemyRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalUdemyRequest)
        `when`(udemyRequestRepository.save(any(UdemyRequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<UdemyRequestUpdateFailedException> { udemyRequestService.editRequest(mockUdemyRequestUpdatingTO, UUID.randomUUID().toString(), testEmail) }
    }

    @Test
    fun `Delete udemy request, should return true`() {
        `when`(udemyRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalUdemyRequest)
        `when`(udemyRequestRepository.delete(any(UdemyRequest::class.java))).then {
            // Do nothing
        }
        assertDoesNotThrow { udemyRequestService.deleteRequest(UUID.randomUUID().toString(), testEmail) }
    }

    @Test
    fun `Delete udemy request, should return false when cannot delete`() {
        `when`(udemyRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalUdemyRequest)
        `when`(udemyRequestRepository.delete(any(UdemyRequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<UdemyRequestDeleteFailedException> { udemyRequestService.deleteRequest(UUID.randomUUID().toString(), testEmail) }
    }

    @Test
    fun `Change activation, should return updated entity`() {
        `when`(udemyRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalUdemyRequest)
        `when`(udemyRequestRepository.save(any(UdemyRequest::class.java))).thenReturn(mockUdemyRequest)

        val result = udemyRequestService.changeActivation(UUID.randomUUID().toString(), testEmail)
        assertThat(result.id).isEqualTo(mockUdemyRequest.id.toString())
    }

    @Test
    fun `Change activation, should throw not found exception`() {
        `when`(udemyRequestRepository.findById(any(UUID::class.java))).thenThrow(UdemyRequestNotFoundException())

        assertThrows<UdemyRequestNotFoundException> { udemyRequestService.changeActivation(UUID.randomUUID().toString(), testEmail) }
    }

    @Test
    fun `Change activation, should throw error when cannot save`() {
        `when`(udemyRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalUdemyRequest)
        `when`(udemyRequestRepository.save(any(UdemyRequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<UdemyRequestUpdateFailedException> { udemyRequestService.changeActivation(UUID.randomUUID().toString(), testEmail) }
    }

    @Test
    fun `Archive udemy request, should work without any exception`() {
        `when`(udemyRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalUdemyRequest)
        `when`(udemyRequestRepository.save(any(UdemyRequest::class.java))).thenReturn(mockUdemyRequest)


        assertDoesNotThrow { udemyRequestService.archive(UUID.randomUUID().toString(), testEmail) }
    }

    @Test
    fun `Archive udemy request, should throw not found exception`() {
        `when`(udemyRequestRepository.findById(any(UUID::class.java))).thenThrow(UdemyRequestNotFoundException())

        assertThrows<UdemyRequestNotFoundException> { udemyRequestService.archive(UUID.randomUUID().toString(), testEmail) }
    }

    @Test
    fun `Archive udemy request, should throw exception`() {
        `when`(udemyRequestRepository.findById(any(UUID::class.java))).thenReturn(mockOptionalUdemyRequest)
        `when`(udemyRequestRepository.save(any(UdemyRequest::class.java))).thenThrow(IllegalArgumentException())

        assertThrows<UdemyRequestUpdateFailedException> { udemyRequestService.archive(UUID.randomUUID().toString(), testEmail) }
    }
}