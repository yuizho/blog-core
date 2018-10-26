package io.github.yuizho.blog.application.controllers

import io.github.yuizho.blog.domain.models.Tag
import io.github.yuizho.blog.infrastructure.repositories.TagRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tags")
class TagController(private val tagRepository: TagRepository) {
    @GetMapping("")
    fun findAll(): Iterable<Tag> = tagRepository.findAll().filter { t -> t.attachedCount() > 0 }
}