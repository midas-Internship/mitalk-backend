package com.example.mitalk.domain.counsellor.presentation

import com.example.mitalk.domain.counsellor.presentation.data.request.ActivityStatusRequest
import com.example.mitalk.domain.counsellor.presentation.data.response.ActivityStatusResponse
import com.example.mitalk.domain.counsellor.service.ActiveStatusService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/counsellor")
@RestController
class CounsellorController(
    private val activeStatusService: ActiveStatusService
) {
    @PostMapping("/activity")
    fun activeStatus(@RequestBody activityStatusRequest: ActivityStatusRequest): ActivityStatusResponse {
        return activeStatusService.execute(activityStatusRequest)
    }
}