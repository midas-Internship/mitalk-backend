package com.example.mitalk.domain.customer.domain

enum class ReviewItem(
        private val description: String
) {
    KINDNESS("친절해요"),
    EXPLANATION("설명이 좋아요"),
    USEFUL("유용해요"),
    COMFORT("편안해요"),
    LISTEN("잘 들어줘요"),
    FASTANSWER("답변이 빨라요");
}