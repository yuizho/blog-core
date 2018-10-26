package io.github.yuizho.blog.infrastructure.repositories

import io.github.yuizho.blog.domain.models.*
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface ArticleRepository : CrudRepository<Article, Long> {
    fun findAllByOrderByAddedAtDesc(): Iterable<Article>

    @Query("select distinct a from Article a join a.tags t where t.name in ?1")
    fun findByTag(tags: List<String>): Iterable<Article>
}

interface TagRepository : CrudRepository<Tag, Long> {
    @Query("select t from Tag t where t.name in ?1")
    fun findByName(tags: List<String>): Iterable<Tag>
}

interface UserRepository : CrudRepository<User, String>

interface LoggeinRepository : CrudRepository<Loggedin, String> {
    @Modifying
    @Query("delete from Loggedin l where l.user.id = ?1")
    fun deleteByUser(id: String): Unit

    @Query("select l from Loggedin l where l.token = ?1")
    fun findByToken(token: Token): Loggedin?
}

interface UploadedRepository : CrudRepository<Uploaded, Long>