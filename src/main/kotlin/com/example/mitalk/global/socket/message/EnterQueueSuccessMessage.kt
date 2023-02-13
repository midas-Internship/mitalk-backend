package com.example.mitalk.global.socket.message

import com.example.mitalk.global.socket.message.element.Message
import com.example.mitalk.global.socket.message.element.MessageType

class EnterQueueSuccessMessage(
    val rank: Long
): Message(MessageType.SYSTEM_1_1_1)
