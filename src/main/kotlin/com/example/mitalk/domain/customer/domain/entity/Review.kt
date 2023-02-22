package com.example.mitalk.domain.customer.domain.entity

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Review(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        val star: Int? = null,

        val message: String? = null,

        @Column(columnDefinition = "BINARY(16)")
        val counsellor: UUID? = null
) {
}