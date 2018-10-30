package io.github.yuizho.blog.infrastructure.repositories

import io.github.yuizho.blog.WebMvcConfig
import io.github.yuizho.blog.domain.models.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest(excludeAutoConfiguration = [WebMvcConfig::class])
class RepositoriesTests(@Autowired val entityManager: TestEntityManager,
                        @Autowired val articleRepository: ArticleRepository,
                        @Autowired val tagRepository: TagRepository,
                        @Autowired val userRepository: UserRepository,
                        @Autowired val loggeinRepository: LoggeinRepository) {
    @Test
    fun `findBytag returns expected multiple Articles`() {
        val (article1, article2) = persistTestArticleData()

        var actual = articleRepository.findByTag(listOf("python"))

        assertThat(actual).contains(article1).contains(article2)
    }

    @Test
    fun `findBytag returns expected single Articles`() {
        val (article1, article2) = persistTestArticleData()

        var actual = articleRepository.findByTag(listOf("ruby"))

        assertThat(actual).hasSize(1).contains(article1)
    }

    @Test
    fun `findBytag with multiple tag returns expected Articles`() {
        val (article1, article2) = persistTestArticleData()

        var actual = articleRepository.findByTag(listOf("python", "ruby"))

        assertThat(actual).hasSize(2).contains(article1).contains(article2)
    }

    @Test
    fun `findByName returns expected Tags`() {
        val (pythonTag, rubyTag) = persistTestTagData()

        val actual = tagRepository.findByName(listOf("python", "ruby"))

        assertThat(actual).hasSize(2).contains(pythonTag).contains(rubyTag)
    }

    @Test
    fun `findByAppId returns expected User`() {
        val sourceUser = persistTestUserData()

        val actual = userRepository.findByAppId(sourceUser.id)

        assertThat(actual).isEqualTo(sourceUser)
    }

    @Test
    fun `findByToken returns expected Loggedin`() {
        val user = persistTestUserData()
        val loggedin = persistLoggedinData(user)

        val actual = loggeinRepository.findByToken(loggedin.token)

        assertThat(actual).isEqualTo(loggedin)
    }

    @Test
    fun `deleteByuser deletes expected Loggedin`() {
        val user = persistTestUserData()
        val loggedin = persistLoggedinData(user)

        loggeinRepository.deleteByUser(user.id)
        entityManager.flush()
        val actual = loggeinRepository.findByToken(loggedin.token)

        assertThat(actual).isNull()
    }

    fun persistTestUserData(): User {
        val sourceUser = User(id = "test_user", password = Password("pass"))
        entityManager.persist(sourceUser)
        entityManager.flush()
        return sourceUser
    }

    fun persistTestTagData(): Pair<Tag, Tag> {
        val pythonTag = Tag(name = "python")
        val rubyTag = Tag(name = "ruby")
        entityManager.persist(pythonTag)
        entityManager.persist(rubyTag)
        entityManager.flush()
        return Pair(pythonTag, rubyTag)
    }

    fun persistTestArticleData(): Pair<Article, Article> {
        val user = persistTestUserData()

        val (pythonTag, rubyTag) = persistTestTagData()

        val article1 = Article(
                Title("タイトル1"), Content("あいうえお1"),
                user, tags = listOf<Tag>(pythonTag, rubyTag))
        val article2 = Article(
                Title("タイトル2"), Content("あいうえお2"),
                user, tags = listOf(pythonTag))
        entityManager.persist(article1)
        entityManager.persist(article2)
        entityManager.flush()

        return Pair(article1, article2)
    }

    fun persistLoggedinData(user: User): Loggedin {
        val loggedin = Loggedin(user = user)
        entityManager.persist(loggedin)
        entityManager.flush()
        return loggedin
    }
}