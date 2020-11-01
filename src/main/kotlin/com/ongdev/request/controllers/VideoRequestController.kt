package com.ongdev.request.controllers

import com.ongdev.request.models.dtos.VideoRequestTO
import com.ongdev.request.services.VideoRequestService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @PostMapping
    fun createVideoRequest(@RequestBody(required = true) videoRequestTO: VideoRequestTO) : ResponseEntity<VideoRequestTO> {
        return ResponseEntity(videoRequestService.createRequest(videoRequestTO), HttpStatus.OK)
    }

    @PutMapping("{id}")
    fun editVideoRequest(
            @PathVariable id: String,
            @RequestBody(required = true) videoRequestTO: VideoRequestTO) : ResponseEntity<VideoRequestTO> {
        return ResponseEntity(videoRequestService.editRequest(videoRequestTO, id), HttpStatus.OK)
    }

    @DeleteMapping("{id}")
    fun deleteVideoRequest(
            @PathVariable id: String
    ) : ResponseEntity<Any> {
        return ResponseEntity(videoRequestService.deleteRequest(id), HttpStatus.OK)
    }

    @GetMapping("{id}/changeActivation")
    fun changeActivation(
            @PathVariable id: String
    ) : ResponseEntity<VideoRequestTO> {
        return ResponseEntity(videoRequestService.changeActivation(id), HttpStatus.OK)
    }

    @GetMapping("{id}/archive")
    fun archiveVideoRequest(
            @PathVariable id: String
    ) : ResponseEntity<Any> {
        return ResponseEntity(videoRequestService.archive(id), HttpStatus.OK)
    }
}