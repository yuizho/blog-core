package io.github.yuizho.blog.application.services

import io.github.yuizho.blog.LocalUploadProperties
import io.github.yuizho.blog.UploadProperties
import io.github.yuizho.blog.application.exceptions.BadRequestException
import io.github.yuizho.blog.application.exceptions.SystemException
import io.github.yuizho.blog.domain.models.Token
import io.github.yuizho.blog.domain.models.Uploaded
import io.github.yuizho.blog.domain.models.User
import io.github.yuizho.blog.infrastructure.repositories.LoggeinRepository
import io.github.yuizho.blog.infrastructure.repositories.UploadedRepository
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.awt.image.BufferedImage
import java.io.BufferedOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
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
}

@Service("LocalUpload")
class LocalUploadService(private val repository: UploadedRepository,
                         private val loggedinRepository: LoggeinRepository,
                         private val localUploadProperties: LocalUploadProperties,
                         private val uploadProperties: UploadProperties): UploadService {

    @Transactional
    override fun handleAndStoreImage(base64File: String,
                                     token: String,
                            builder: UriComponentsBuilder): Uploaded {
        // https://www.netmarvs.com/archives/1113
        val sentImageFile : ByteArray = Base64.getDecoder().decode(base64File)
        if (sentImageFile.size > uploadProperties.maxfilesize.toInt()) { throw BadRequestException("the file size is too large (Max: ${uploadProperties.maxfilesize} byte).") }
        val bi: BufferedImage = ImageIO.read(ByteArrayInputStream(sentImageFile)) ?: throw BadRequestException("the file is not image file (jpeg, png, gif, bpm, wbmp)")
        val bos = ByteArrayOutputStream()
        if (!ImageIO.write(bi, localUploadProperties.format, bos)) {
            // probably this error's cause is some programming mistake.
            throw SystemException("unexpected image format.");
        }
        val handledImage: ByteArray = bos.toByteArray()

        // store the file
        // TODO: need to use syncronized?
        val fileName: String =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) +
                        RandomStringUtils.randomAlphanumeric(12) +
                        ".${localUploadProperties.format}"
        val path: Path = Paths.get("${localUploadProperties.path}${fileName}")
        Files.newOutputStream(path).use {
            BufferedOutputStream(it, 1024).use {
                it.write(handledImage)
                it.flush()
            }
        }

        // store the uploaded file information
        val user: User = loggedinRepository.findByToken(Token(token))?.user
                ?: throw SystemException("there is no required loggein record")
        val imageUri : String = builder.path("/${localUploadProperties.pathpattern}/").path("${fileName}").build().toUriString()
        return repository.save(Uploaded(fileName = fileName, fileUri = imageUri, user = user))
    }
}

//@Service("S3Upload")
//class S3UploadService: UploadService{u
//    override fun handleAndStoreImage(base64File: String,
//                                     token: String,
//                                     builder: UriComponentsBuilder): Uploaded = Uploaded(fileName = "s3", imageUri = "via s3", user = User("test", "test"))
//}