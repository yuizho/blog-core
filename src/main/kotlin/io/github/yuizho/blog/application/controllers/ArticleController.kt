package io.github.yuizho.blog.application.controllers

import io.github.yuizho.blog.domain.models.Article
import io.github.yuizho.blog.application.services.ArticleService
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