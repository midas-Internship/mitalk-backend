package com.example.mitalk.domain.customer.domain.entity

import com.example.mitalk.domain.customer.domain.ReviewItem
import java.io.Serializable
import javax.persistence.*

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
                @Enumerated(value = EnumType.STRING)
                @Column(nullable = false)
                var reviewItem: ReviewItem,

                @Column(nullable = false)
                var reviewId: Long
        ) : Serializable
}