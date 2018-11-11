package io.github.yuizho.blog.application.services

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.github.yuizho.blog.LocalUploadProperties
import io.github.yuizho.blog.UploadProperties
import io.github.yuizho.blog.application.exceptions.BadRequestException
import io.github.yuizho.blog.domain.models.*
import io.github.yuizho.blog.infrastructure.LocalFileIO
import io.github.yuizho.blog.infrastructure.repositories.LoggeinRepository
import io.github.yuizho.blog.infrastructure.repositories.UploadedRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UploadServiceTest(@Autowired val uploadProperties: UploadProperties,
                        @Autowired val localUploadProperties: LocalUploadProperties) {
    @Mock
    private lateinit var uploadedRepository: UploadedRepository

    @Mock
    private lateinit var loggedinRepository: LoggeinRepository

    @Mock
    private lateinit var localFileIO: LocalFileIO

    @Test
    fun `handle and save image to local succeeded`() {
        val user = User(id = "user", password = Password("pass"))
        val token = Token("token")
        val uploaded = Uploaded(fileName = "xxx.png", fileUri = "/static/xxx.png", user = user)
        whenever(loggedinRepository.findByToken(Token("token")))
                .thenReturn(Loggedin(token = token, user = user))
        whenever(uploadedRepository.save(any<Uploaded>())).thenReturn(uploaded)

        val localUploadService = LocalUploadService(uploadedRepository,
                loggedinRepository, localUploadProperties, uploadProperties, localFileIO)
        val actual = localUploadService.handleAndStoreImage(
                "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",
                "token")

        assertThat(actual).isEqualTo(uploaded)
    }

    @Test
    fun `the passed base64File is not image`() {
        val localUploadService = LocalUploadService(uploadedRepository,
                loggedinRepository, localUploadProperties, uploadProperties, localFileIO);
        val actualExcetion =
                Assertions.assertThrows(BadRequestException::class.java) {
                    localUploadService.handleAndStoreImage(
                            "yv66vgAAADQAPAEAKWlvL2dpdGh1Yi95dWl6aG8vYmxvZy9CbG9nQ29yZUFwcGxpY2F0aW9uBwABAQAQamF2YS9sYW5nL09iamVjdAcAAwEAPkxvcmcvc3ByaW5nZnJhbWV3b3JrL2Jvb3QvYXV0b2NvbmZpZ3VyZS9TcHJpbmdCb290QXBwbGljYXRpb247AQBLTG9yZy9zcHJpbmdmcmFtZXdvcmsvYm9vdC9jb250ZXh0L3Byb3BlcnRpZXMvRW5hYmxlQ29uZmlndXJhdGlvblByb3BlcnRpZXM7AQAFdmFsdWUBAC1MaW8vZ2l0aHViL3l1aXpoby9ibG9nL0xvY2FsVXBsb2FkUHJvcGVydGllczsBAChMaW8vZ2l0aHViL3l1aXpoby9ibG9nL1VwbG9hZFByb3BlcnRpZXM7AQATZGF0YWJhc2VJbml0aWFsaXplcgEAcChMaW8vZ2l0aHViL3l1aXpoby9ibG9nL2luZnJhc3RydWN0dXJlL3JlcG9zaXRvcmllcy9Vc2VyUmVwb3NpdG9yeTspTG9yZy9zcHJpbmdmcmFtZXdvcmsvYm9vdC9Db21tYW5kTGluZVJ1bm5lcjsBAA51c2VyUmVwb3NpdG9yeQEALUxvcmcvc3ByaW5nZnJhbWV3b3JrL2NvbnRleHQvYW5ub3RhdGlvbi9CZWFuOwEAI0xvcmcvamV0YnJhaW5zL2Fubm90YXRpb25zL05vdE51bGw7CAAMAQAea290bGluL2p2bS9pbnRlcm5hbC9JbnRyaW5zaWNzBwAQAQAXY2hlY2tQYXJhbWV0ZXJJc05vdE51bGwBACcoTGphdmEvbGFuZy9PYmplY3Q7TGphdmEvbGFuZy9TdHJpbmc7KVYMABIAEwoAEQAUAQA/aW8vZ2l0aHViL3l1aXpoby9ibG9nL0Jsb2dDb3JlQXBwbGljYXRpb24kZGF0YWJhc2VJbml0aWFsaXplciQxBwAWAQAGPGluaXQ+AQBFKExpby9naXRodWIveXVpemhvL2Jsb2cvaW5mcmFzdHJ1Y3R1cmUvcmVwb3NpdG9yaWVzL1VzZXJSZXBvc2l0b3J5OylWDAAYABkKABcAGgEAKm9yZy9zcHJpbmdmcmFtZXdvcmsvYm9vdC9Db21tYW5kTGluZVJ1bm5lcgcAHAEABHRoaXMBACtMaW8vZ2l0aHViL3l1aXpoby9ibG9nL0Jsb2dDb3JlQXBwbGljYXRpb247AQBCTGlvL2dpdGh1Yi95dWl6aG8vYmxvZy9pbmZyYXN0cnVjdHVyZS9yZXBvc2l0b3JpZXMvVXNlclJlcG9zaXRvcnk7AQADKClWDAAYACEKAAQAIgEAEUxrb3RsaW4vTWV0YWRhdGE7AQACbXYDAAAAAQMAAAALAQACYnYDAAAAAAMAAAACAQABawEAAmQxAQBFwIAYCgIYAgoCEMCACgIIAgoCGAIKwIAKAhgCCsCACBcYwIAyAjABQgXCogYCEAJKEBADGgIwBDIGEAUaAjAGSBfCqAYHAQACZDIBAAABACxMb3JnL3NwcmluZ2ZyYW1ld29yay9ib290L0NvbW1hbmRMaW5lUnVubmVyOwEACWJsb2ctY29yZQEAFkJsb2dDb3JlQXBwbGljYXRpb24ua3QBAARDb2RlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAPTGluZU51bWJlclRhYmxlAQAQTWV0aG9kUGFyYW1ldGVycwEAGVJ1bnRpbWVWaXNpYmxlQW5ub3RhdGlvbnMBABtSdW50aW1lSW52aXNpYmxlQW5ub3RhdGlvbnMBACRSdW50aW1lSW52aXNpYmxlUGFyYW1ldGVyQW5ub3RhdGlvbnMBAApTb3VyY2VGaWxlAQAMSW5uZXJDbGFzc2VzACEAAgAEAAAAAAACAAEACgALAAUAMwAAAEoAAwACAAAAEisSD7gAFbsAF1krtwAbwAAdsAAAAAIANAAAABYAAgAAABIAHgAfAAAAAAASAAwAIAABADUAAAAKAAIABgAZABEAHgA2AAAABQEADAAAADcAAAAGAAEADQAAADgAAAAGAAEADgAAADkAAAAHAQABAA4AAAABABgAIQABADMAAAAvAAEAAQAAAAUqtwAjsQAAAAIANAAAAAwAAQAAAAUAHgAfAAAANQAAAAYAAQAAABcAAwA6AAAAAgAyADsAAAAKAAEAFwAAAAAAGAA3AAAAXwADAAUAAAAGAAEAB1sAAmMACGMACQAkAAUAJVsAA0kAJkkAJkkAJwAoWwADSQAmSQApSQAqACtJACYALFsAAXMALQAuWwAIcwAfcwAvcwAhcwAKcwAwcwAMcwAgcwAx",
                            "token")
                }
        assertThat(actualExcetion).isNotNull();
    }
}