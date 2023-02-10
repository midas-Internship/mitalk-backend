package com.example.mitalk.domain.customer.domain.repository

import com.example.mitalk.domain.customer.domain.entity.CustomerSession
import org.springframework.data.repository.CrudRepository
import org.springframework.web.socket.WebSocketSession

interface CustomerSessionRepository : CrudRepository<CustomerSession, WebSocketSession> {

}