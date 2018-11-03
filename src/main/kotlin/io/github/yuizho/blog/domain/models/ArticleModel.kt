package io.github.yuizho.blog.domain.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import io.github.gitbucket.markedj.Marked
import io.github.gitbucket.markedj.Options
import org.springframework.http.MediaType
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@JsonIgnoreProperties(value = ["tags", "content"])
data class Article(
        var title: Title,
        var content: Content,
        @ManyToOne @JoinColumn val user: User,
        @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
        val id: Long? = null,
        val addedAt: LocalDateTime = LocalDateTime.now(),
        var modifiedAt: LocalDateTime = LocalDateTime.now(),
        @ManyToMany
        @JoinTable(
                name = "article_has_tag",
                joinColumns = [JoinColumn(name = "article_id")],
                inverseJoinColumns = [JoinColumn(name = "tag_id")]
        )
        var tags: List<Tag> = emptyList()) {

    @JsonProperty("tag_names")
    fun tagNames() = tags.map { t -> t.name }
}

@Embeddable
class Title(title: String) {
    @JsonValue val title: String

    init {
        // TODO: do sanitizing or something
        this.title = title
    }
}

@Embeddable
class Content(content: String) {
    @JsonValue val content: String

    init {
        this.content = content
    }

    fun renderAsHtml(): String {
        val options = Options().apply {
            isSanitize = true
        }
        return Marked.marked(content, options)
    }

    fun renderAsMarkdown(): String {
        return content;
    }

    fun render(renderType: String?): Pair<String, MediaType> = when(renderType) {
        "html" -> Pair(renderAsHtml(), MediaType.TEXT_HTML)
        else -> Pair(renderAsMarkdown(), MediaType.TEXT_PLAIN)
    }
}

@Entity
data class Uploaded(
        @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
        val id: Long? = null,
        val fileName: String,
        @Column(unique = true) val fileUri: String,
        @ManyToOne @JoinColumn val user: User)