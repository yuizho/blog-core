package io.github.yuizho.blog.domain.models

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class LoggedinModelTests {
    @Test
    fun `Loggedin#isExpired works properly`() {
        val user = User(id = "test_user", password = Password("12345"))

        assertThat(Loggedin(user = user).isExpired()).isFalse()
        assertThat(Loggedin(expiredAt = LocalDateTime.now().minusDays(1), user = user).isExpired()).isTrue()
    }

    @Test
    fun `Token#isExpectedScheme works properly`() {
        assertThat(Token("token 9595000c-63da-4743-a6a2-272531c9dd63").isExpectedScheme())
                .isTrue();
        assertThat(Token("xxxx 9595000c-63da-4743-a6a2-272531c9dd63").isExpectedScheme())
                .isFalse()
    }

    @Test
    fun `Token#equals works properly`() {
        val token1 = Token("token 9595000c-63da-4743-a6a2-272531c9dd63")
        val token2 = Token("token 9595000c-63da-4743-a6a2-272531c9dd63")
        val token3 = Token("token dd95000c-63da-4743-a6a2-272531c9dd63")

        assertThat(token1.equals(token2)).isTrue()
        assertThat(token1.equals(token3)).isFalse()
    }
}