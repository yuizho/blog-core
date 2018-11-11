package io.github.yuizho.blog.infrastructure

import com.amazonaws.ClientConfiguration
import com.amazonaws.Protocol
import com.amazonaws.SdkClientException
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import io.github.yuizho.blog.application.exceptions.SystemException
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.net.URL

@Component
class S3Client {
    companion object {
        val S3_ACCESS_KEY: String? = System.getenv("S3_ACCESS_KEY")
        val S3_SECRET_KEY: String? = System.getenv("S3_SECRET_KEY")
        val S3_END_POINT: String? = System.getenv("S3_END_POINT")
        val S3_REGION: String? = System.getenv("S3_REGION")
        val S3_BUCKET_NAME: String? = System.getenv("S3_BUCKET_NAME")
    }

    fun isS3InformationInValid(): Boolean {
        return S3_ACCESS_KEY == null || S3_SECRET_KEY == null || S3_END_POINT == null ||
                S3_REGION == null || S3_BUCKET_NAME == null
    }

    fun uploadAndGetUrl(fileName: String, image: ByteArray): URL {
        if (isS3InformationInValid()) {
            throw SystemException("S3Upload feature need to set S3_ACCESS_KEY, S3_SECRET_KEY, S3_END_POINT, S3_REGION, S3_BUCKET_NAME environment variables")
        }

        val s3Client = AmazonS3ClientBuilder
                .standard().withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(S3_ACCESS_KEY, S3_SECRET_KEY)))
                .withClientConfiguration(ClientConfiguration().apply {
                    protocol = Protocol.HTTPS
                    connectionTimeout = 10000
                })
                .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(S3_END_POINT, S3_REGION))
                .build()

        ByteArrayInputStream(image).use {
            val metaData = ObjectMetadata().apply {
                contentLength = image.size.toLong()
                contentType = "image/png"

            }
            try {
                s3Client.putObject(PutObjectRequest(S3_BUCKET_NAME, fileName, it, metaData)
                        .withCannedAcl(CannedAccessControlList.PublicRead))
            } catch (e: SdkClientException) {
                throw SystemException(e.message ?: "")
            }
        }

        return s3Client.getUrl(S3_BUCKET_NAME, fileName)
    }
}