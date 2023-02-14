package com.example.mitalk.domain.image.presentation.data

import org.springframework.web.multipart.MultipartFile

data class UploadFileDto(
        val files: List<MultipartFile>
)
