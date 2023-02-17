package com.example.mitalk.domain.admin.service

import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.customer.domain.entity.CustomerQueue
import com.example.mitalk.domain.customer.domain.repository.CustomerInfoRepository
import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import com.example.mitalk.global.socket.util.SessionUtils
import org.springframework.stereotype.Service

@Service
class AdminResetService(
    private val sessionUtils: SessionUtils,
    private val customerInfoRepository: CustomerInfoRepository,
    private val customerRepository: CustomerRepository,
    private val counsellorRepository: CounsellorRepository,
    private val customerQueue: CustomerQueue
) {

    fun execute() {
        sessionUtils.removeAll()
        customerInfoRepository.deleteAll()
        customerRepository.deleteAll()
        counsellorRepository.deleteAll()
        customerQueue.zRangeAndDelete(0, -1)
    }
}