package com.example.mitalk.domain.customer.domain

enum class ReviewItem(
        private val description: String
) {
    // 별점 3개 이상
    KINDNESS("친절해요"),
    EXPLANATION("설명이 좋아요"),
    USEFUL("유용해요"),
    COMFORT("편안해요"),
    LISTEN("잘 들어줘요"),
    FAST_ANSWER("답변이 빨라요"),

    // 별점 2개 이하
    UNKINDNESS("불친절해요"),
    DIFFICULT_EXPLANATION("어려운 설명이에요"),
    USELESS("유용하지 않음"),
    SLANG("비속어"),
    NOT_APPROPRIATE_ANSWER("적절하지 않은 답변"),
    SLOW_ANSWER("느린 답변시간");
}