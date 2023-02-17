package com.example.mitalk.domain.customer.domain.entity

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

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