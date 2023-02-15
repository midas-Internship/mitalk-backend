package com.example.mitalk.global.batch

import com.example.mitalk.domain.counsellor.domain.entity.Counsellor
import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.counsellor.domain.entity.CounsellorStatus
import com.example.mitalk.domain.record.domain.repository.RecordRepository
import com.example.mitalk.global.redis.util.CustomerQueueRedisUtils
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
    private val customerQueueRedisUtils: CustomerQueueRedisUtils,
    private val messageUtils: MessageUtils,
    private val sessionUtils: SessionUtils,
    private val recordRepository: RecordRepository
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
        val customerSize = customerQueueRedisUtils.zSize()

        if (counsellorSize > customerSize) {
            connection(
                counsellorList = counsellorList.subList(0, customerSize.toInt()),
                customerList = customerQueueRedisUtils.zRangeAndDelete(1, customerSize)
            )
        }
        else {
            connection(
                counsellorList = counsellorList,
                customerList = customerQueueRedisUtils.zRangeAndDelete(1, counsellorSize.toLong())
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

                messageUtils.sendSystemMessage(message, sessionUtils.get(a.counsellorSession!!))
                messageUtils.sendSystemMessage(message, sessionUtils.get(b))
            }
        }
    }

    private fun sendCurrentQueueOrder() {
        if (customerQueueRedisUtils.zSize() < 1) return;

        customerQueueRedisUtils.zRange(1, -1).forEachIndexed {
                index, s -> messageUtils.sendSystemMessage(CurrentQueueMessage(index + 1L), sessionUtils.get(s))
        }
    }
}