package com.example.mitalk.global.batch

import com.example.mitalk.domain.counsellor.domain.entity.Counsellor
import com.example.mitalk.domain.counsellor.domain.entity.CounsellorStatus
import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.customer.domain.entity.CustomerQueue
import com.example.mitalk.domain.customer.domain.repository.CustomerInfoRepository
import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import com.example.mitalk.domain.record.domain.entity.Record
import com.example.mitalk.domain.record.domain.repository.RecordRepository
import com.example.mitalk.global.socket.message.CounsellingStartMessage
import com.example.mitalk.global.socket.message.CurrentQueueMessage
import com.example.mitalk.global.socket.util.MessageUtils
import com.example.mitalk.global.socket.util.SessionUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@Component
class Scheduler(
    private val counsellorRepository: CounsellorRepository,
    private val customerQueue: CustomerQueue,
    private val messageUtils: MessageUtils,
    private val sessionUtils: SessionUtils,
    private val recordRepository: RecordRepository,
    private val customerInfoRepository: CustomerInfoRepository,
    private val customerRepository: CustomerRepository,
) {

    @Scheduled(cron = "*/7 * * * * *")
    fun queueSchedule() {
        val counsellorList = counsellorRepository.findByStatusOrderByTodayCounsellingCountAsc(CounsellorStatus.ONLINE)

        if (counsellorList.isEmpty() || customerQueue.zSize() == 0L) {
            println("counsellor or customer empty!!")
            sendCurrentQueueOrder()
            return
        }

        val counsellorSize = counsellorList.size
        val customerSize = customerQueue.zSize()

        if (counsellorSize > customerSize) {

            println("counsellorsize > customersize")
            connection(
                counsellorList = counsellorList.subList(0, customerSize.toInt()),
                customerList = customerQueue.zRangeAndDelete(0, customerSize)
            )
        }
        else {

            println("counsellorsize < customersize")
            connection(
                counsellorList = counsellorList,
                customerList = customerQueue.zRangeAndDelete(0, counsellorSize.toLong())
            )
        }
        sendCurrentQueueOrder()
    }

    private fun connection(counsellorList: List<Counsellor>, customerList: List<String>) {
        counsellorList.forEachIndexed { index, counsellor ->
            val roomId = UUID.randomUUID()
            val message = CounsellingStartMessage(roomId)

            counsellorRepository.save(
                counsellor.counsellingEvent(roomId, customerList[index])
            )
            val customerInfo = customerInfoRepository.findByCustomerSessionId(customerList[index])!!
            recordRepository.save(
                Record(
                    id = roomId,
                    customerId = customerInfo.customerId,
                    customerName = (customerRepository.findByIdOrNull(customerInfo.customerId) ?: TODO("Customer notfound")).name,
                    counsellorId = counsellor.id,
                    counsellorName = (counsellorRepository.findByIdOrNull(counsellor.id) ?: TODO("Counsellor notfound")).name,
                    counsellingType = customerInfo.type
                )
            )

            messageUtils.sendSystemMessage(message, sessionUtils.get(counsellor.counsellorSession!!))
            messageUtils.sendSystemMessage(message, sessionUtils.get(customerList[index]))
        }
    }

    private fun sendCurrentQueueOrder() {
        if (customerQueue.zSize() < 1) return;

        println(" " + customerQueue.zRange(0, -1))
        customerQueue.zRange(0, -1).forEachIndexed {
                index, s -> messageUtils.sendSystemMessage(CurrentQueueMessage(index + 1L), sessionUtils.get(s))
        }
    }
}