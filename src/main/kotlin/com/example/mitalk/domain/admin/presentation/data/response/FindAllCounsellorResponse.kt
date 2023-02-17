package com.example.mitalk.domain.admin.presentation.data.response

import com.example.mitalk.domain.counsellor.domain.entity.CounsellorStatus
import java.util.UUID

class FindAllCounsellorResponse(
        val counsellorId: UUID?,

        val name: String,

        val status: CounsellorStatus
) {
}