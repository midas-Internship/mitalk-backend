package com.example.mitalk.domain.auth.exception

import com.example.mitalk.global.exception.ErrorCode
import com.example.mitalk.global.exception.exceptions.BasicException

class ExpiredRefreshTokenException : BasicException(ErrorCode.EXPIRED_TOKEN) {
}