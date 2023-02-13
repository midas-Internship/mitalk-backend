package com.example.mitalk.global.batch

import com.example.mitalk.domain.counsellor.persistence.Counsellor
import com.example.mitalk.domain.counsellor.persistence.CounsellorRepository
import com.example.mitalk.domain.counsellor.persistence.CounsellorStatus
import com.example.mitalk.global.redis.util.CustomerQueueRedisUtils
import com.example.mitalk.global.socket.message.CounsellingStartMessage
import com.example.mitalk.global.socket.util.ChatUtils
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import java.util.*

@Component
class Scheduler(
    private val counsellorRepository: CounsellorRepository,
    private val customerQueueRedisUtils: CustomerQueueRedisUtils,
    private val chatUtils: ChatUtils
) {

    @Scheduled(cron = "*/30 * * * * *")
    fun queueSchedule() {
        println("scheduler실행 (30초마다)")
        val counsellorList = counsellorRepository.findByStatusOrderByTodayCounsellingCountAsc(CounsellorStatus.ONLINE)

        if (counsellorList.isEmpty()) return;

        val counsellorSize = counsellorList.size
        val customerSize = customerQueueRedisUtils.zSize()

        if (counsellorSize > customerSize) {
            connection(
                counsellorList = counsellorList.subList(0, customerSize.toInt()),
                customerList = customerQueueRedisUtils.zRange(0, customerSize)
            )
        }
        else {
            connection(
                counsellorList = counsellorList,
                customerList = customerQueueRedisUtils.zRange(0, counsellorSize.toLong())
            )
        }

    }

    private fun connection(counsellorList: List<Counsellor>, customerList: List<Any>) {
        counsellorList.zip(customerList) { a: Counsellor, b: Any -> {
                val roomId = UUID.randomUUID()
                val message = CounsellingStartMessage(roomId)

               a.counsellingEvent(roomId, b as WebSocketSession)

                chatUtils.sendMessage(message, a.counsellorSession!!)
                chatUtils.sendMessage(message, a.customerSession!!)
            }
        }
    }
}