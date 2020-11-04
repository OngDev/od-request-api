package com.ongdev.request.controllers

import com.ongdev.request.models.dtos.qna.QARequestCreationTO
import com.ongdev.request.models.dtos.qna.QARequestTO
import com.ongdev.request.models.dtos.qna.QARequestUpdatingTO
import com.ongdev.request.services.QARequestService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/qna")
class QARequestController(private val qaRequestService: QARequestService) {
    @GetMapping
    fun getQARequests(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    direction = Sort.Direction.DESC,
                    sort = ["title"]) pageable: Pageable)
            : ResponseEntity<Page<QARequestTO>>{
        return ResponseEntity(qaRequestService.getRequests(pageable), HttpStatus.OK)
    }

    @PostMapping
    fun createQARequest(
            @RequestBody(required = true) qaRequestCreationTO: QARequestCreationTO,
            principal: Principal) : ResponseEntity<QARequestTO> {
        return ResponseEntity(qaRequestService.createRequest(qaRequestCreationTO, principal.name), HttpStatus.OK)
    }

    @PutMapping("{id}")
    fun editQARequest(
            @PathVariable id: String,
            @RequestBody(required = true) qaRequestUpdatingTO: QARequestUpdatingTO,
            principal: Principal) : ResponseEntity<QARequestTO> {
        return ResponseEntity(qaRequestService.editRequest(qaRequestUpdatingTO, id, principal.name), HttpStatus.OK)
    }

    @DeleteMapping("{id}")
    fun deleteQARequest(
            @PathVariable id: String,
            principal: Principal
    ) : ResponseEntity<Any> {
        return ResponseEntity(qaRequestService.deleteRequest(id, principal.name), HttpStatus.OK)
    }

    @GetMapping("{id}/changeActivation")
    fun changeActivation(
            @PathVariable id: String,
            principal: Principal
    ) : ResponseEntity<QARequestTO> {
        return ResponseEntity(qaRequestService.changeActivation(id, principal.name), HttpStatus.OK)
    }

    @GetMapping("{id}/archive")
    fun archiveQARequest(
            @PathVariable id: String,
            principal: Principal
    ) : ResponseEntity<Any> {
        return ResponseEntity(qaRequestService.archive(id, principal.name), HttpStatus.OK)
    }

    @GetMapping("{id}/upvote")
    fun upVote(
            @PathVariable id: String,
            principal: Principal
    ) : ResponseEntity<Any> {
        return ResponseEntity(qaRequestService.upVote(id, principal.name), HttpStatus.OK)
    }

    @GetMapping("{id}/downvote")
    fun downVote(
            @PathVariable id: String,
            principal: Principal
    ) : ResponseEntity<Any> {
        return ResponseEntity(qaRequestService.downVote(id, principal.name), HttpStatus.OK)
    }
}