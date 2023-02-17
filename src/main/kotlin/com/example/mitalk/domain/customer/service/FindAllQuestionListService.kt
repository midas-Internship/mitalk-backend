package com.example.mitalk.domain.customer.service

import com.example.mitalk.domain.customer.domain.repository.QuestionRepository
import com.example.mitalk.domain.customer.presentation.data.response.QuestionListResponse
import com.example.mitalk.global.util.UserUtil
import org.springframework.stereotype.Service

@Service
class FindAllQuestionListService(
        private val userUtil: UserUtil,
        private val questionRepository: QuestionRepository

) {
    fun execute(): List<QuestionListResponse> {
        val customer = userUtil.getCurrentCustomer()
        return questionRepository.findAll()
                .map { QuestionListResponse(it.id, it.question, it.answer) }
    }
}