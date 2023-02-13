package com.example.mitalk.domain.customer.exception

import com.example.mitalk.global.exception.ErrorCode
import com.example.mitalk.global.exception.exceptions.BasicException

class CustomerNotFoundException : BasicException(ErrorCode.CUSTOMER_NOT_FOUND) {
}