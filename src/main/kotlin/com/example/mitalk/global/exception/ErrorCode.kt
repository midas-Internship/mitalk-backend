package com.example.mitalk.global.exception

enum class ErrorCode(
        val message: String,
        val status: Int
) {
    FILE_SIZE_OVER("파일의 크기가 1024MB보다 큼", 400),

    UNAUTHORIZED("권한이 없음", 401),
    EXPIRED_TOKEN("만료된 토큰", 401),

    CUSTOMER_NOT_FOUND("해당 고객을 찾을 수 없음", 404),
    COUNSELLOR_NOT_FOUND("해당 상담사를 찾을 수 없음", 404),
    OFFICIALS_NOT_FOUND("해당 관계자를 찾을 수 없음", 404),

    INTERNAL_SERVER_ERROR("서버 내부 에러", 500),
}