package com.example.mitalk.global.socket.util

import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import java.util.WeakHashMap

@Component
class SessionUtils {
    companion object{
        var SessionFactory: WeakHashMap<String, WebSocketSession> = WeakHashMap()
    }

    fun get(sessionId: String): WebSocketSession {
        return SessionFactory.get(sessionId) ?: TODO("now found Exception")
    }

    fun remove(sessionId: String) {
        SessionFactory.remove(sessionId)
    }

    fun add(session: WebSocketSession): String {
        SessionFactory.put(session.id, session)

        return session.id
    }

    fun removeAll() {
        SessionFactory = WeakHashMap()
    }
}