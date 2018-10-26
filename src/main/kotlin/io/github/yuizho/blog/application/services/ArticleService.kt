package io.github.yuizho.blog.application.services

import io.github.yuizho.blog.application.exceptions.NotFoundException
import io.github.yuizho.blog.domain.models.*
import io.github.yuizho.blog.infrastructure.repositories.ArticleRepository
import io.github.yuizho.blog.infrastructure.repositories.LoggeinRepository
import io.github.yuizho.blog.infrastructure.repositories.TagRepository
import io.github.yuizho.blog.infrastructure.repositories.UserRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam
import javax.transaction.SystemException
import javax.transaction.Transactional

@Service("ArticleService")
class ArticleService(private val articleRepository: ArticleRepository,
                     private val tagRepository: TagRepository,
                     private val loggedinRepository: LoggeinRepository) {

    fun findAll(tags: List<String>?): Iterable<Article> {
        if (tags?.isEmpty() ?: true) {
            return articleRepository.findAllByOrderByAddedAtDesc()
        }
        return articleRepository.findByTag(tags!!)
    }

    fun findOne(id: Long)
            = articleRepository.findById(id).orElseThrow { NotFoundException("no target article.") }

    fun modify(id: Long, title: String?, content: String?, tags: List<String>?): Article {
        val article: Article
                = articleRepository.findById(id).orElseThrow { NotFoundException("no target article.") }
        val registeredTags = saveAndReturnRegisteredTags(tags)

        title?.let { article.title = Title(it)}
        content?.let { article.content = Content(it) }
        tags?.let { article.tags = registeredTags }
        articleRepository.save(article)
        return article
    }

    fun delete(id: Long): Unit = articleRepository.deleteById(id)

    fun create(title: String, content: String, token: String, tags: List<String>?): Article {
        val registeredTags = saveAndReturnRegisteredTags(tags)

        // save the article
        return articleRepository.save(Article(
                Title(title), Content(content),
                loggedinRepository.findByToken(Token(token))?.user
                        ?: throw SystemException("there is no required loggein record"),
                tags = registeredTags))

    }

    private fun saveAndReturnRegisteredTags(tags: List<String>?): List<Tag> {
        val alreadyRegisteredTags = tags?.let { tagRepository.findByName(it) } ?: emptyList()
        val needToRegisterTagNames = (tags ?: emptyList()).minus(alreadyRegisteredTags.map { t -> t.name })
        val needToRegisterTags = needToRegisterTagNames.map { tagName -> Tag(name = tagName) }
        val registeredTags
                = needToRegisterTags.let { if (!it.isEmpty()) tagRepository.saveAll(it) else emptyList() }
        return (alreadyRegisteredTags + registeredTags)
    }
}