package com.example.mitalk.global.socket.message

import com.example.mitalk.global.socket.message.element.Message
import com.example.mitalk.global.socket.message.element.MessageType

class QueueAlreadyFilledMessage(
): Message(MessageType.SYSTEM_1_1_2)
