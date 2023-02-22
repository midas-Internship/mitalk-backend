package com.example.mitalk.domain.admin.exception

import com.example.mitalk.global.exception.ErrorCode
import com.example.mitalk.global.exception.exceptions.BasicException

class AdminNotFoundException : BasicException(ErrorCode.ADMIN_NOT_FOUND) {
}