package com.example.mitalk.domain.customer.service

import com.example.mitalk.domain.customer.domain.repository.QuestionRepository
import com.example.mitalk.domain.customer.presentation.data.response.QuestionListResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FindAllQuestionListService(
        private val questionRepository: QuestionRepository

) {
    @Transactional(readOnly = true)
    fun execute(): List<QuestionListResponse> {
        return questionRepository.findAll()
                .map { QuestionListResponse(it.id, it.question, it.answer) }
    }
}