package io.github.yuizho.blog.domain.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonValue
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@JsonIgnoreProperties(value = ["expired_at", "user", "expired"])
data class Loggedin(
        @Id val token: Token = Token(UUID.randomUUID()),
        val expiredAt: LocalDateTime = LocalDateTime.now().plusDays(30),
        @OneToOne @JoinColumn() val user: User) {

    fun isExpired(): Boolean = expiredAt.isBefore(LocalDateTime.now())
}

@Embeddable
@JsonIgnoreProperties(value = ["scheme", "expected_scheme"])
class Token: Serializable {
    @Transient val scheme: String
    @JsonValue val token: String

    constructor(uuid: UUID) {
        this.scheme = "token"
        this.token = uuid.toString()
    }

    constructor(tokenWithScheme: String) {
        this.scheme = tokenWithScheme.substringBefore(" ").trim()
        this.token = tokenWithScheme.substringAfter(" ").trim()
    }

    fun isExpectedScheme(): Boolean = this.scheme == "token"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Token

        if (token != other.token) return false

        return true
    }

    override fun hashCode(): Int {
        return token.hashCode()
    }
}