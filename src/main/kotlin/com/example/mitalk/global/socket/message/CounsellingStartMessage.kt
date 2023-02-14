package com.example.mitalk.global.socket.message

import com.example.mitalk.global.socket.message.element.SystemMessage
import com.example.mitalk.global.socket.message.element.MessageType
import java.util.UUID

class CounsellingStartMessage(
    val roomId: UUID
): SystemMessage(MessageType.SYSTEM_3_1)