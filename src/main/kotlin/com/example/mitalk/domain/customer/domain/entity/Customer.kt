package com.example.mitalk.domain.customer.domain.entity

import com.example.mitalk.domain.auth.domain.Role
import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import javax.persistence.*

@Entity
class Customer(
        @Id
        @GeneratedValue(generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "uuid2")
        @Column(columnDefinition = "BINARY(16)")
        val id: UUID,

        val name: String,

        val email: String,

        val picture: String,

        var needReview: UUID? = null,
)