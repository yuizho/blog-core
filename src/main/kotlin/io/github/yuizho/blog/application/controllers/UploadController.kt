package io.github.yuizho.blog.application.controllers

import io.github.yuizho.blog.domain.models.Uploaded
import io.github.yuizho.blog.application.services.UploadService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/upload")
class UploadController(@Qualifier("uploadServiceFactory") private val uploadService: UploadService) {

    @PostMapping("/{name}")
    fun upload(@NotNull @RequestParam(value="base64_file") base64File: String,
               @NotNull @RequestHeader(value="Authorization") token: String): Uploaded = uploadService.handleAndStoreImage(base64File, token)
}