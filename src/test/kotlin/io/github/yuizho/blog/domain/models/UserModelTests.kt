package io.github.yuizho.blog.domain.models

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserModelTests {
    @Test
    fun `Password#isSameAs compares Password value properly`() {
        val user = User(id = "test_user", password = Password("12345"))

        assertThat(user.password.isSameAs("12345aa")).isFalse()
        assertThat(user.password.isSameAs("12345")).isTrue()
    }
}