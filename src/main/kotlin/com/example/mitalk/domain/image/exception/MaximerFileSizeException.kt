package com.example.mitalk.domain.image.exception

import com.example.mitalk.global.exception.ErrorCode
import com.example.mitalk.global.exception.exceptions.BasicException

class MaximerFileSizeException : BasicException(ErrorCode.FILE_SIZE_OVER) {
}