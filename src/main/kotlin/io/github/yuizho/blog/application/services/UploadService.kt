package io.github.yuizho.blog.application.services

import io.github.yuizho.blog.LocalUploadProperties
import io.github.yuizho.blog.UploadProperties
import io.github.yuizho.blog.application.exceptions.BadRequestException
import io.github.yuizho.blog.application.exceptions.SystemException
import io.github.yuizho.blog.domain.models.Token
import io.github.yuizho.blog.domain.models.Uploaded
import io.github.yuizho.blog.infrastructure.LocalFileIO
import io.github.yuizho.blog.infrastructure.repositories.LoggeinRepository
import io.github.yuizho.blog.infrastructure.repositories.UploadedRepository
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.imageio.ImageIO
import javax.transaction.Transactional

interface UploadService {
    fun handleAndStoreImage(base64File: String,
                            token: String,
                            builder: UriComponentsBuilder): Uploaded
    fun generateFileName(imageFormat: String): String =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) +
                    RandomStringUtils.randomAlphanumeric(10) +
                    ".$imageFormat"

    fun convertBase64ToBinaryImage(base64File: String, maxFileSize: Int, imageFormat: String): ByteArray {
        val sentImageFile : ByteArray = Base64.getDecoder().decode(base64File)
        if (sentImageFile.size > maxFileSize) { throw BadRequestException("the file size is too large (Max: ${maxFileSize} byte).") }
        val bi: BufferedImage = ImageIO.read(ByteArrayInputStream(sentImageFile)) ?: throw BadRequestException("the file is not image file (jpeg, png, gif, bpm, wbmp)")
        val bos = ByteArrayOutputStream()
        if (!ImageIO.write(bi, imageFormat, bos)) {
            // probably this error's cause is some programming mistake.
            throw SystemException("unexpected image format.");
        }
        return bos.toByteArray()
    }
}

@Service("LocalUpload")
class LocalUploadService(private val repository: UploadedRepository,
                         private val loggedinRepository: LoggeinRepository,
                         private val localUploadProperties: LocalUploadProperties,
                         private val uploadProperties: UploadProperties,
                         private val localFileIO: LocalFileIO): UploadService {

    @Transactional
    override fun handleAndStoreImage(base64File: String,
                                     token: String,
                            builder: UriComponentsBuilder): Uploaded {
        // https://www.netmarvs.com/archives/1113
        val handledImage
                = convertBase64ToBinaryImage(base64File, uploadProperties.maxfilesize.toInt(), uploadProperties.format)

        val fileName = generateFileName(uploadProperties.format)

        // store the uploaded file information
        val user = loggedinRepository.findByToken(Token(token))?.user ?: throw SystemException("there is no required loggein record")
        val imageUri = builder.path("/${localUploadProperties.pathpattern}/").path("${fileName}").build().toUriString()
        val uploaded = repository.save(Uploaded(fileName = fileName, fileUri = imageUri, user = user))

        // store the file
        localFileIO.write(Paths.get("${localUploadProperties.path}${fileName}"), handledImage)

        return uploaded
    }
}

@Service("S3Upload")
class S3UploadService: UploadService{
    override fun handleAndStoreImage(base64File: String,
                                     token: String,
                                     builder: UriComponentsBuilder): Uploaded {
        throw NotImplementedError("no implementation.")
    }
}