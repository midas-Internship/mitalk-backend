package com.example.mitalk.domain.counsellor.exception

import com.example.mitalk.global.exception.ErrorCode
import com.example.mitalk.global.exception.exceptions.BasicException

class CounsellorNotFoundException : BasicException(ErrorCode.COUNSELLOR_NOT_FOUND) {
}