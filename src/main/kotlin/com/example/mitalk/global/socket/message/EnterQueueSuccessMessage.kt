package com.example.mitalk.global.socket.message

import com.example.mitalk.global.socket.message.element.SystemMessage
import com.example.mitalk.global.socket.message.element.MessageType

class EnterQueueSuccessMessage(
    val order: Long
): SystemMessage(MessageType.SYSTEM_1_1_1)
