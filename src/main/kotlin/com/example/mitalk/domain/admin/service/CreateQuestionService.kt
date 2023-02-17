package com.example.mitalk.domain.admin.service

import com.example.mitalk.domain.admin.exception.AlreadyExistsQuestionException
import com.example.mitalk.domain.admin.presentation.data.request.CreateQuestionRequest
import com.example.mitalk.domain.customer.domain.entity.Question
import com.example.mitalk.domain.customer.domain.repository.QuestionRepository

class CreateQuestionService(
        private val questionRepository: QuestionRepository
) {
    fun createQuestion(createQuestionRequest: CreateQuestionRequest) {
        questionRepository.findByQuestion(createQuestionRequest.question) ?: throw AlreadyExistsQuestionException()
        val question = Question(0, createQuestionRequest.question, createQuestionRequest.answer)
        questionRepository.save(question)
    }
}