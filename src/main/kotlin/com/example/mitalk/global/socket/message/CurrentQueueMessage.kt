package com.example.mitalk.global.socket.message

import com.example.mitalk.global.socket.message.element.MessageType
import com.example.mitalk.global.socket.message.element.SystemMessage

class CurrentQueueMessage(
    val order: Long
): SystemMessage(MessageType.SYSTEM_1_2)