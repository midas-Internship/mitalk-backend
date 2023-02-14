package com.example.mitalk.domain.image.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.example.mitalk.domain.image.exception.MaximerFileSizeException
import com.example.mitalk.domain.image.presentation.data.FileDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class UploadFileService(
        private val amazonS3: AmazonS3,
) {
    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    @Value("\${cloud.aws.s3.url}")
    lateinit var url: String

    fun execute(dto: MultipartFile): FileDto {
        var result = ""
        println("bytes 사이즈 " + dto.bytes.size)
        val fileName = createFileName()
        val objectMetadata = ObjectMetadata()
        objectMetadata.contentLength = dto.size
        objectMetadata.contentType = dto.contentType

        try {
            dto.inputStream.use { inputStream ->
                amazonS3.putObject(
                        PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead)
                )
            }
        } catch (e: Exception) {
            when (e.cause) {
                is MaxUploadSizeExceededException -> throw MaximerFileSizeException()
            }
        }
        result = url + fileName
        return FileDto(file = result)
    }

}

private fun createFileName(): String {
    return UUID.randomUUID().toString()
}