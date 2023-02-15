package com.example.mitalk.domain.customer.domain.repository

import com.example.mitalk.domain.customer.domain.entity.ReviewElement
import org.springframework.data.repository.CrudRepository

interface ReviewElementRepository : CrudRepository<ReviewElement, ReviewElement.ReviewElementId> {
}