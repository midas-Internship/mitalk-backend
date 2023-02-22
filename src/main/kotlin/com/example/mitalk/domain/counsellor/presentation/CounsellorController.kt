package com.example.mitalk.domain.counsellor.presentation

import com.example.mitalk.domain.counsellor.presentation.data.request.ActivityStatusRequest
import com.example.mitalk.domain.counsellor.presentation.data.response.ActivityStatusResponse
import com.example.mitalk.domain.counsellor.presentation.data.response.FindActivityStatusResponse
import com.example.mitalk.domain.counsellor.service.ActiveStatusService
import com.example.mitalk.domain.counsellor.service.FindActivityStatusService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/counsellor")
@RestController
class CounsellorController(
//    private val activeStatusService: ActiveStatusService
        private val findActivityStatusService: FindActivityStatusService
) {
//    @PostMapping("/activity")
//    fun activeStatus(@RequestBody activityStatusRequest: ActivityStatusRequest): ActivityStatusResponse {
//        return activeStatusService.execute(activityStatusRequest)
//    }

    @GetMapping("/activity")
    fun findActivityStatus(): FindActivityStatusResponse {
        return findActivityStatusService.execute()
    }
}