package com.example.mitalk.domain.image.presentation.data

import org.springframework.web.multipart.MultipartFile

data class UploadImagesDto(
        val files: List<MultipartFile>
)
