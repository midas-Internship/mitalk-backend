package com.example.mitalk.global.security.exception

import com.example.mitalk.global.exception.ErrorCode
import com.example.mitalk.global.exception.exceptions.BasicException

class InvalidTokenException : BasicException(ErrorCode.UNAUTHORIZED)