package com.example.mitalk.domain.admin.presentation

import com.example.mitalk.domain.admin.presentation.data.request.CreateCounsellorRequest
import com.example.mitalk.domain.admin.presentation.data.request.UpdateQuestionRequest
import com.example.mitalk.domain.admin.presentation.data.response.CreateCounsellorResponse
import com.example.mitalk.domain.admin.presentation.data.response.FindAllCounsellorResponse
import com.example.mitalk.domain.admin.presentation.data.response.FindQuestionResponse
import com.example.mitalk.domain.admin.presentation.data.response.GetRecordListRequest
import com.example.mitalk.domain.admin.service.*
import com.example.mitalk.domain.customer.presentation.data.response.QuestionListResponse
import com.example.mitalk.domain.customer.service.FindAllQuestionListService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminController(
        private val createCounsellorService: CreateCounsellorService,
        private val findAllCounsellorService: FindAllCounsellorService,
        private val fidAllQuestionService: FindAllQuestionService,
        private val updateQuestionService: UpdateQuestionService,
        private val getRecordListService: GetRecordListService
) {
    @PostMapping("/counsellor")
    fun createCounsellor(@RequestBody createCounsellorRequest: CreateCounsellorRequest): ResponseEntity<CreateCounsellorResponse> =
            createCounsellorService.execute(createCounsellorRequest)
                    .let { ResponseEntity(it, HttpStatus.CREATED) }

    @GetMapping("/counsellor")
    fun findAllCounsellor(): List<FindAllCounsellorResponse> = findAllCounsellorService.execute()

    @GetMapping("/question")
    fun findAllQuestion(): List<FindQuestionResponse> = fidAllQuestionService.execute()

    @PatchMapping("/question/{question-id}")
    fun updateQuestion(@PathVariable("question-id") questionId: Long, @RequestBody request: UpdateQuestionRequest) {
        updateQuestionService.execute(questionId, request)
    }

    @GetMapping("/record")
    fun getRecordList(): List<GetRecordListRequest> = getRecordListService.execute()
}