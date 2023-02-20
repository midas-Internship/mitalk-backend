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

    val customerName: String,

    val counsellorId: UUID,

    val counsellorName: String,

    val counsellingType: CounsellingType,

    val messageRecords: MutableList<MessageRecord> = mutableListOf()
) {
    fun add(messageRecord: MessageRecord): Record {
        messageRecords.add(messageRecord)
        return Record(
            id, startAt, customerId, customerName, counsellorId, counsellorName, counsellingType, messageRecords
        )
    }
    fun findMessageRecordById(messageId: UUID): MessageRecord? {
        return messageRecords.find { it.messageId == messageId }
    }

    fun updateMessageRecord(messageRecord: MessageRecord): Record {
        messageRecords.filter { it.messageId == messageRecord.messageId }.map{ messageRecord }
        return this
    }
}

data class MessageRecord(
    val messageId: UUID,

    val sender: Role,

    val isFile: Boolean,

    val isDeleted: Boolean,

    val isUpdated: Boolean,

    val dataMap: MutableList<MessageData>
) {

    data class MessageData(
        val message: String,
        val localDateTime: LocalDateTime
    )

    fun updateData(data: String): MessageRecord {
        if (isDeleted) {
            //todo throw is already deleted message
            return this
        }
        isTimeOverLimit()

        dataMap.add(MessageData(data, LocalDateTime.now()))
        return MessageRecord(messageId, sender, isFile,
            isDeleted = false,
            isUpdated = true,
            dataMap = dataMap
        )
    }

    fun deleteData(): MessageRecord {
        isTimeOverLimit()
        return MessageRecord(messageId, sender, isFile, isDeleted = true, isUpdated, dataMap)
    }

    private fun isTimeOverLimit() {
        if (getCurrentPair().localDateTime.plusSeconds(300).isBefore(LocalDateTime.now())) TODO("throw is already over limited")
    }

    private fun getCurrentPair(): MessageData {
        return if (isUpdated) {
            dataMap.last()
        } else {
            dataMap.first()
        }
    }

}