package com.example.mitalk.domain.auth.exception

import com.example.mitalk.global.exception.ErrorCode
import com.example.mitalk.global.exception.exceptions.BasicException

class OfficialsNotFoundException : BasicException(ErrorCode.OFFICIALS_NOT_FOUND) {
}