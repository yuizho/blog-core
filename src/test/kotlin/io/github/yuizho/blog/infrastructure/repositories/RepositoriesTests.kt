package io.github.yuizho.blog.infrastructure.repositories

import io.github.yuizho.blog.WebMvcConfig
import io.github.yuizho.blog.domain.models.Password
import io.github.yuizho.blog.domain.models.User
import io.github.yuizho.blog.infrastructure.repositories.UserRepository
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
                        @Autowired val userRepository: UserRepository) {
    @Test
    fun `When findByAppId then return expected User`() {
        val sourceUser = User(id = "test_user", password = Password("pass"))
        entityManager.persist(sourceUser)
        entityManager.flush()

        val actual = userRepository.findByAppId(sourceUser.id)

        assertThat(actual).isEqualTo(sourceUser)
    }
}