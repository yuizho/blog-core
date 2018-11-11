package io.github.yuizho.blog

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("upload")
class UploadProperties {
    lateinit var maxfilesize: String
    lateinit var format: String
}

@ConfigurationProperties("upload.local")
class LocalUploadProperties {
    lateinit var path: String
    lateinit var pathpattern: String
}