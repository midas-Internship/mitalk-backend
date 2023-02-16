package com.example.mitalk.domain.customer.service

import com.example.mitalk.domain.customer.domain.repository.QuestionRepository
import com.example.mitalk.domain.customer.presentation.data.response.QuestionListResponse
import com.example.mitalk.global.util.CustomerUtil
import org.springframework.stereotype.Service

@Service
class FindAllQuestionListService(
        private val customerUtil: CustomerUtil,
        private val questionRepository: QuestionRepository

) {
    fun execute(): List<QuestionListResponse> {
        val customer = customerUtil.getCurrentCustomer()
        val questions = questionRepository.findAll()
                .map { QuestionListResponse(it.question, it.answer) }
        return questions
    }
}