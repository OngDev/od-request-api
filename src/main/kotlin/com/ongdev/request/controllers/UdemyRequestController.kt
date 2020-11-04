package com.ongdev.request.controllers

import com.ongdev.request.models.dtos.udemy.UdemyRequestCreationTO
import com.ongdev.request.models.dtos.udemy.UdemyRequestTO
import com.ongdev.request.models.dtos.udemy.UdemyRequestUpdatingTO
import com.ongdev.request.services.UdemyRequestService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/udemy")
class UdemyRequestController(private val udemyRequestService: UdemyRequestService) {
    @GetMapping
    fun getUdemyRequests(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    direction = Sort.Direction.DESC,
                    sort = ["title"]) pageable: Pageable)
            : ResponseEntity<Page<UdemyRequestTO>>{
        return ResponseEntity(udemyRequestService.getRequests(pageable), HttpStatus.OK)
    }

    @PostMapping
    fun createUdemyRequest(
            @RequestBody(required = true) udemyRequestCreationTO: UdemyRequestCreationTO,
            principal: Principal) : ResponseEntity<UdemyRequestTO> {
        return ResponseEntity(udemyRequestService.createRequest(udemyRequestCreationTO, principal.name), HttpStatus.OK)
    }

    @PutMapping("{id}")
    fun editUdemyRequest(
            @PathVariable id: String,
            @RequestBody(required = true) udemyRequestUpdatingTO: UdemyRequestUpdatingTO,
            principal: Principal) : ResponseEntity<UdemyRequestTO> {
        return ResponseEntity(udemyRequestService.editRequest(udemyRequestUpdatingTO, id, principal.name), HttpStatus.OK)
    }

    @DeleteMapping("{id}")
    fun deleteUdemyRequest(
            @PathVariable id: String,
            principal: Principal
    ) : ResponseEntity<Any> {
        return ResponseEntity(udemyRequestService.deleteRequest(id, principal.name), HttpStatus.OK)
    }

    @GetMapping("{id}/changeActivation")
    fun changeActivation(
            @PathVariable id: String,
            principal: Principal
    ) : ResponseEntity<UdemyRequestTO> {
        return ResponseEntity(udemyRequestService.changeActivation(id, principal.name), HttpStatus.OK)
    }

    @GetMapping("{id}/archive")
    fun archiveUdemyRequest(
            @PathVariable id: String,
            principal: Principal
    ) : ResponseEntity<Any> {
        return ResponseEntity(udemyRequestService.archive(id, principal.name), HttpStatus.OK)
    }

    @GetMapping("{id}/upvote")
    fun upVote(
            @PathVariable id: String,
            principal: Principal
    ) : ResponseEntity<Any> {
        return ResponseEntity(udemyRequestService.upVote(id, principal.name), HttpStatus.OK)
    }

    @GetMapping("{id}/downvote")
    fun downVote(
            @PathVariable id: String,
            principal: Principal
    ) : ResponseEntity<Any> {
        return ResponseEntity(udemyRequestService.downVote(id, principal.name), HttpStatus.OK)
    }
}