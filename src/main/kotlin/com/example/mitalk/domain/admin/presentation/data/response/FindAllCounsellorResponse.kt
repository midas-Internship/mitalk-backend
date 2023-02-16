package com.example.mitalk.domain.admin.presentation.data.response

import com.example.mitalk.domain.counsellor.domain.entity.CounsellorStatus
import java.util.UUID

class FindAllCounsellorResponse(
        val counsellorId: UUID?,

        val roomId: UUID?,

        val name: String,

        val counsellorSession: String?,

        val customerSession: String?,

        val todayCounsellingCount: Int,

        val status: CounsellorStatus

) {
}