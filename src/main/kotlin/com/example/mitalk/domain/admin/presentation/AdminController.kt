package com.example.mitalk.domain.admin.presentation

import com.example.mitalk.domain.admin.presentation.data.request.CreateCounsellorRequest
import com.example.mitalk.domain.admin.presentation.data.response.CreateCounsellorResponse
import com.example.mitalk.domain.admin.presentation.data.response.FindAllCounsellorResponse
import com.example.mitalk.domain.admin.service.CreateCounsellorService
import com.example.mitalk.domain.admin.service.FindAllCounsellorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminController(
        private val createCounsellorService: CreateCounsellorService,
        private val findAllCounsellorService: FindAllCounsellorService
) {
    @PostMapping("/counsellor")
    fun createCounsellor(@RequestBody createCounsellorRequest: CreateCounsellorRequest): ResponseEntity<CreateCounsellorResponse> =
            createCounsellorService.execute(createCounsellorRequest)
                    .let { ResponseEntity(it, HttpStatus.CREATED) }

    @GetMapping("/counsellor")
    fun findAllCounsellor(): List<FindAllCounsellorResponse> = findAllCounsellorService.execute()
}