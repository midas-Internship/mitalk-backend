package com.example.mitalk.domain.record.domain.entity

import com.example.mitalk.domain.auth.domain.Role
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document("record")
data class Record(
    @Id
    val id: UUID,

    val startAt: LocalDateTime = LocalDateTime.now(),

    val customerId: UUID,

    val counsellorId: UUID,

    val messageRecords: MutableList<MessageRecord> = mutableListOf()
) {
    fun add(messageRecord: MessageRecord): Record {
        messageRecords.add(messageRecord)
        return Record(
            id, startAt, customerId, counsellorId, messageRecords
        )
    }
}

data class MessageRecord(
    val sender: Role,

    val isFile: Boolean,

    val isDeleted: Boolean,

    val isUpdated: Boolean,

    val dataMap: LinkedHashMap<String, LocalDateTime>
) {

    fun updateData(data: String): MessageRecord {
        if (isDeleted) {
            //todo throw is already deleted message
            return this
        }
        isTimeOverLimit()

        dataMap.put(data, LocalDateTime.now())
        return MessageRecord(sender, isFile,
            isDeleted = false,
            isUpdated = true,
            dataMap = dataMap
        )
    }

    fun deleteData(): MessageRecord {
        isTimeOverLimit()
        return MessageRecord(sender, isFile, isDeleted = true, isUpdated, dataMap)
    }

    private fun isTimeOverLimit() {
        if (getCurrentPair().second.plusSeconds(300).isBefore(LocalDateTime.now())) TODO("throw is already over limited")
    }

    private fun getCurrentPair(): Pair<String, LocalDateTime> {
        return if (isUpdated) {
            dataMap.entries.last().toPair()
        } else {
            dataMap.entries.iterator().next().toPair()
        }
    }

}