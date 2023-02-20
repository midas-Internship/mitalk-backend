package com.example.mitalk.global.bean

import com.example.mitalk.domain.customer.domain.entity.CustomerQueue
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class InitCustomerQueue(
    private val customerQueue: CustomerQueue,
) {

    @PostConstruct
    fun execute() {
        customerQueue.zRangeAndDelete(0, -1)
    }
}