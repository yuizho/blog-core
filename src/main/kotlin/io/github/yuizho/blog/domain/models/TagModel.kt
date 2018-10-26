package io.github.yuizho.blog.domain.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@JsonIgnoreProperties(value = ["id","articles"])
data class Tag(
        @Id @GeneratedValue val id: Long? = null,
        @Column(unique = true) val name: String,
        @ManyToMany(mappedBy = "tags")
        val articles: List<Article> = emptyList()) {

        @JsonProperty("attached_count")
        fun attachedCount() = articles.size
}