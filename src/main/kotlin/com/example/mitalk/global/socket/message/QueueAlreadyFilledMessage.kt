package com.example.mitalk.global.socket.message

import com.example.mitalk.global.socket.message.element.SystemMessage
import com.example.mitalk.global.socket.message.element.MessageType

class QueueAlreadyFilledMessage(
): SystemMessage(MessageType.SYSTEM_1_1_2)
