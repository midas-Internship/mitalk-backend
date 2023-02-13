package com.example.mitalk.global.exception.handler

import com.example.mitalk.global.exception.ErrorCode
import com.example.mitalk.global.exception.ErrorResponse
import com.example.mitalk.global.exception.exceptions.BasicException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BasicException::class)
    fun globalExceptionHandler(e: BasicException) : ResponseEntity<ErrorResponse> {
        val errorCode: ErrorCode = e.errorCode
        return ResponseEntity(
                ErrorResponse(status = errorCode.status, message = errorCode.message),
                HttpStatus.valueOf(errorCode.status)
        )
    }
}