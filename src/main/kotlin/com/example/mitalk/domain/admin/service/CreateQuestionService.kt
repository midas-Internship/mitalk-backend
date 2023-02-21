package com.example.mitalk.domain.admin.service

import com.example.mitalk.domain.admin.exception.AlreadyExistsQuestionException
import com.example.mitalk.domain.admin.presentation.data.request.CreateQuestionRequest
import com.example.mitalk.domain.customer.domain.entity.Question
import com.example.mitalk.domain.customer.domain.repository.QuestionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateQuestionService(
        private val questionRepository: QuestionRepository
) {
    @Transactional
    fun execute(createQuestionRequest: CreateQuestionRequest) {
        val existsByQuestion = questionRepository.existsByQuestion(createQuestionRequest.question)
        if(existsByQuestion) {
            throw AlreadyExistsQuestionException()
        }
        val question = Question(0, createQuestionRequest.question, createQuestionRequest.answer)
        questionRepository.save(question)
    }
}