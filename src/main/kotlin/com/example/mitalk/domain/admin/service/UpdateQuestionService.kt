package com.example.mitalk.domain.admin.service

import com.example.mitalk.domain.admin.presentation.data.request.UpdateQuestionRequest
import com.example.mitalk.domain.customer.domain.entity.Question
import com.example.mitalk.domain.customer.domain.repository.QuestionRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UpdateQuestionService(
    private val questionRepository: QuestionRepository
) {

    fun execute(questionId: Long, request: UpdateQuestionRequest) {

        val question = questionRepository.findByIdOrNull(questionId) ?: TODO("questionNotFoundexception")

        questionRepository.save(
            Question(
                id = question.id,
                question = request.question,
                answer = request.answer
            )
        )
    }
}