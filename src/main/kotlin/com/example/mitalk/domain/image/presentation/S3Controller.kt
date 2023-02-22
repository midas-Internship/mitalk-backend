package com.example.mitalk.domain.image.presentation

import com.example.mitalk.domain.image.presentation.data.FileDto
import com.example.mitalk.domain.image.service.UploadFileService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/file")
class S3Controller(
        private val uploadFileService: UploadFileService
) {
    @PostMapping
    fun fileUpload(@RequestParam(value = "file", required = false) multipartFile: MultipartFile): FileDto {
        return uploadFileService.execute(multipartFile)
    }
}