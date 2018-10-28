package io.github.yuizho.blog.domain.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.persistence.*

@Entity
@JsonIgnoreProperties(value = ["system_id", "password"])
data class User(
        @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
        val systemId: Long? = null,
        @Column(unique = true) var id: String,
        var password: Password)

@Embeddable
@JsonIgnoreProperties(value = ["password"])
class Password(password: String) {
    private val password: String = BCryptPasswordEncoder().encode(password)

    fun isSameAs(password: String): Boolean = BCryptPasswordEncoder().matches(password, this.password)
}