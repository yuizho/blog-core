package io.github.yuizho.blog.infrastructure

import org.springframework.stereotype.Component
import java.io.BufferedOutputStream
import java.nio.file.Files
import java.nio.file.Path

@Component
class LocalFileIO {
    @Synchronized
    fun write(path: Path, binary: ByteArray) {
        Files.newOutputStream(path).use {
            BufferedOutputStream(it, 1024).use {
                it.write(binary)
                it.flush()
            }
        }
    }
}