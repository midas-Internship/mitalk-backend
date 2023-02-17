package com.example.mitalk.domain.admin.exception

import com.example.mitalk.global.exception.ErrorCode
import com.example.mitalk.global.exception.exceptions.BasicException

class AlreadyExistsQuestionException : BasicException(ErrorCode.ALREADY_EXISTS_QUESTION) {
}