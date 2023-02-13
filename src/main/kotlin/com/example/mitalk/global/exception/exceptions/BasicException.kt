package com.example.mitalk.global.exception.exceptions

import com.example.mitalk.global.exception.ErrorCode

open class BasicException(val errorCode: ErrorCode): RuntimeException()