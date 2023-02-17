package com.example.mitalk.global.batch

import com.example.mitalk.domain.counsellor.domain.entity.Counsellor
import com.example.mitalk.domain.counsellor.domain.entity.CounsellorStatus
import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.customer.domain.entity.CustomerQueue
import com.example.mitalk.domain.customer.domain.repository.CustomerInfoRepository
import com.example.mitalk.domain.record.domain.entity.Record
import com.example.mitalk.domain.record.domain.repository.RecordRepository
import com.example.mitalk.global.socket.message.CounsellingStartMessage
import com.example.mitalk.global.socket.message.CurrentQueueMessage
import com.example.mitalk.global.socket.util.MessageUtils
import com.example.mitalk.global.socket.util.SessionUtils
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
    private val customerInfoRepository: CustomerInfoRepository
) {

    @Scheduled(cron = "*/7 * * * * *")
    fun queueSchedule() {
        println("scheduler실행 (7초마다)")
        val counsellorList = counsellorRepository.findByStatusOrderByTodayCounsellingCountAsc(CounsellorStatus.ONLINE)

        if (counsellorList.isEmpty()) {
            sendCurrentQueueOrder()
            return
        }

        val counsellorSize = counsellorList.size
        val customerSize = customerQueue.zSize()

        if (counsellorSize > customerSize) {
            connection(
                counsellorList = counsellorList.subList(0, customerSize.toInt()),
                customerList = customerQueue.zRangeAndDelete(1, customerSize)
            )
        }
        else {
            connection(
                counsellorList = counsellorList,
                customerList = customerQueue.zRangeAndDelete(1, counsellorSize.toLong())
            )
        }
        sendCurrentQueueOrder()
    }

    private fun connection(counsellorList: List<Counsellor>, customerList: List<String>) {
        counsellorList.zip(customerList) { a: Counsellor, b: String -> {
            val roomId = UUID.randomUUID()
            val message = CounsellingStartMessage(roomId)

            counsellorRepository.save(
                a.counsellingEvent(roomId, b)
            )

            val customerInfo = customerInfoRepository.findByCustomerSessionId(b)!!
            recordRepository.save(
                Record(
                    id = roomId,
                    customerId = customerInfo.customerId,
                    counsellorId = a.id!!,
                    counsellingType = customerInfo.type
                )
            )

            messageUtils.sendSystemMessage(message, sessionUtils.get(a.counsellorSession!!))
            messageUtils.sendSystemMessage(message, sessionUtils.get(b))
        }

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