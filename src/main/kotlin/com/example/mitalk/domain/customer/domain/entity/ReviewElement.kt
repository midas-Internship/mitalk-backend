package com.example.mitalk.domain.customer.domain.entity

import com.example.mitalk.domain.customer.domain.ReviewItem
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MapsId

@Entity
class ReviewElement(
        @EmbeddedId
        val id: ReviewElementId,

        @MapsId("reviewId")
        @ManyToOne
        @JoinColumn(name = "review_id")
        val review: Review
) {
        @Embeddable
        data class ReviewElementId(
                @Column(nullable = false)
                var reviewItem: ReviewItem,

                @Column(nullable = false)
                var reviewId: Long
        ) : Serializable
}