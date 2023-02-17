package com.example.mitalk.domain.admin.service

import com.example.mitalk.domain.admin.presentation.data.response.FindQuestionResponse
import com.example.mitalk.domain.customer.domain.repository.QuestionRepository
import org.springframework.stereotype.Service

@Service
class FindAllQuestionService(
    private val questionRepository: QuestionRepository
) {

    fun execute(): List<FindQuestionResponse> {
        return questionRepository.findAll().map {
            FindQuestionResponse(
                id = it.id,
                question = it.question,
                answer = it.answer
            )
        }
    }
}