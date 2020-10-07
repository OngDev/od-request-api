package com.ongdev.request.controllers

import com.ongdev.request.models.dtos.VideoRequestTO
import com.ongdev.request.services.VideoRequestService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/videos")
class VideoRequestController(private val videoRequestService: VideoRequestService) {
    @GetMapping
    fun getVideoRequests(pageable: Pageable) : ResponseEntity<Page<VideoRequestTO>>{
        return ResponseEntity(videoRequestService.getVideoRequests(pageable), HttpStatus.OK)
    }
}