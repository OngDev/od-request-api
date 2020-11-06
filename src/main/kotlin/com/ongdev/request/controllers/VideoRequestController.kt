package com.ongdev.request.controllers

import com.ongdev.request.models.dtos.video.VideoRequestCreationTO
import com.ongdev.request.models.dtos.video.VideoRequestTO
import com.ongdev.request.models.dtos.video.VideoRequestUpdatingTO
import com.ongdev.request.services.VideoRequestService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/videos")
class VideoRequestController(private val videoRequestService: VideoRequestService) {
    @GetMapping
    fun getVideoRequests(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    direction = Sort.Direction.DESC,
                    sort = ["title"]) pageable: Pageable)
            : ResponseEntity<Page<VideoRequestTO>>{
        return ResponseEntity(videoRequestService.getRequests(pageable), HttpStatus.OK)
    }

    @GetMapping("/mine")
    fun getMyVideoRequests(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    direction = Sort.Direction.DESC,
                    sort = ["title"]) pageable: Pageable,
            principal: Principal)
            : ResponseEntity<Page<VideoRequestTO>>{
        return ResponseEntity(videoRequestService.getMyRequests(pageable, principal.name), HttpStatus.OK)
    }

    @PostMapping
    fun createVideoRequest(
            @RequestBody(required = true) videoRequestCreationTO: VideoRequestCreationTO,
            principal: Principal) : ResponseEntity<VideoRequestTO> {
        return ResponseEntity(videoRequestService.createRequest(videoRequestCreationTO, principal.name), HttpStatus.OK)
    }

    @PutMapping("{id}")
    fun editVideoRequest(
            @PathVariable id: String,
            @RequestBody(required = true) videoRequestUpdatingTO: VideoRequestUpdatingTO,
            principal: Principal) : ResponseEntity<VideoRequestTO> {
        return ResponseEntity(videoRequestService.editRequest(videoRequestUpdatingTO, id, principal.name), HttpStatus.OK)
    }

    @DeleteMapping("{id}")
    fun deleteVideoRequest(
            @PathVariable id: String,
            principal: Principal
    ) : ResponseEntity<Any> {
        return ResponseEntity(videoRequestService.deleteRequest(id, principal.name), HttpStatus.OK)
    }

    @GetMapping("{id}/changeActivation")
    fun changeActivation(
            @PathVariable id: String,
            principal: Principal
    ) : ResponseEntity<VideoRequestTO> {
        return ResponseEntity(videoRequestService.changeActivation(id, principal.name), HttpStatus.OK)
    }

    @GetMapping("{id}/archive")
    fun archiveVideoRequest(
            @PathVariable id: String,
            principal: Principal
    ) : ResponseEntity<Any> {
        return ResponseEntity(videoRequestService.archive(id, principal.name), HttpStatus.OK)
    }

    @GetMapping("{id}/upvote")
    fun upVote(
            @PathVariable id: String,
            principal: Principal
    ) : ResponseEntity<Any> {
        return ResponseEntity(videoRequestService.upVote(id, principal.name), HttpStatus.OK)
    }

    @GetMapping("{id}/downvote")
    fun downVote(
            @PathVariable id: String,
            principal: Principal
    ) : ResponseEntity<Any> {
        return ResponseEntity(videoRequestService.downVote(id, principal.name), HttpStatus.OK)
    }
}