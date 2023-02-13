package com.example.mitalk.global.socket.message

import com.example.mitalk.global.socket.message.element.Message
import com.example.mitalk.global.socket.message.element.MessageType
import java.util.UUID


class CounsellingStartMessage(
    val roomId: UUID
): Message(MessageType.SYSTEM_3_1)