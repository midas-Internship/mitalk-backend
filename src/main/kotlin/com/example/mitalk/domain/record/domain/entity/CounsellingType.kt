package com.example.mitalk.domain.record.domain.entity

enum class CounsellingType(
    private val message: String
) {

    FEATURE_QUESTION("기능 질문"),
    FEEDBACK("제품 피드백"),
    BUG("버그 제보"),
    FEATURE_PROPOSAL("기능 제안"),
    PURCHASE("제휴 문의"),
    ETC("기타");

    fun message(): String = message
}
