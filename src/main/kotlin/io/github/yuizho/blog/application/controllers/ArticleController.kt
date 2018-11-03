package io.github.yuizho.blog.application.controllers

import io.github.yuizho.blog.domain.models.Article
import io.github.yuizho.blog.application.services.ArticleService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/articles")
class ArticleController(private val articleService: ArticleService) {
    @GetMapping("")
    fun findAll(@RequestParam tags: List<String>?): Iterable<Article>
            = articleService.findAll(tags)

    @GetMapping("/{id}")
    fun findOne(@PathVariable id: Long): Article
            = articleService.findOne(id)

    @GetMapping("/{id}/content",
            produces = [MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE])
    fun findContent(@PathVariable id: Long,
                    @RequestParam render: String?): ResponseEntity<String> {
        val (body, mediaType) = articleService.findContent(id, render)

        val headers = HttpHeaders().apply {
            add("Content-Type", "$mediaType; charset=utf-8")
            // TODO: how to add this header as default
            add("X-Content-Type-Options", "nosniff")
        }
        return ResponseEntity(body, headers, HttpStatus.OK)
    }

    @PutMapping("/{id}")
    fun modify(@PathVariable id: Long,
               @NotNull @RequestParam title: String?,
               @NotNull @RequestParam content: String?,
               @RequestParam tags: List<String>?): Article
            = articleService.modify(id, title, content, tags)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): Unit
        = articleService.delete(id)

    @PostMapping("")
    fun create(@NotNull @RequestParam title: String,
               @NotNull @RequestParam content: String,
               @NotNull @RequestHeader(value = "Authorization") token: String,
               @RequestParam tags: List<String>?,
               response: HttpServletResponse): Article {
        response.status = HttpServletResponse.SC_CREATED
        return articleService.create(title, content, token, tags)
    }
}