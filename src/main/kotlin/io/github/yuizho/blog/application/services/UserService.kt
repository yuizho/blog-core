package io.github.yuizho.blog.application.services

import io.github.yuizho.blog.application.exceptions.BadRequestException
import io.github.yuizho.blog.application.exceptions.SystemException
import io.github.yuizho.blog.application.exceptions.UnauthorizedException
import io.github.yuizho.blog.domain.models.Loggedin
import io.github.yuizho.blog.domain.models.Password
import io.github.yuizho.blog.domain.models.Token
import io.github.yuizho.blog.domain.models.User
import io.github.yuizho.blog.infrastructure.repositories.LoggeinRepository
import io.github.yuizho.blog.infrastructure.repositories.UserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("LoginService")
class LoginService(private val repository: UserRepository,
                   private val loggeinRepository: LoggeinRepository) {
    @Transactional
    fun login(id: String, sentPass: String): Loggedin {
        val user: User = repository.findByAppId(id)
                ?: throw UnauthorizedException("id or password is wrong.")
        if (!user.password.isSameAs(sentPass)) { throw UnauthorizedException("id or password is wrong.") }
        loggeinRepository.deleteByUser(user.id)
        return loggeinRepository.save(Loggedin(user = user))
    }

    fun logout(token: String): Unit {
        loggeinRepository.findByToken(Token(token))?.let {
            loggeinRepository.delete(it)
        }
    }

    fun findUser(token: String): User {
        val loggedin: Loggedin = loggeinRepository.findByToken(Token(token))
                ?: throw SystemException("the token should have been authorized. it's provably programing bug.")
        return loggedin.user
    }

    fun modifyUser(id: String?, sentPass: String?, token: String): User {
        val user = findUser(token)
        id?.let { i ->
            if (repository.findByAppId(i) != null)
                throw BadRequestException("the id is already used.")
            user.id = i
        }
        sentPass?.let { user.password = Password(sentPass) }
        return repository.save(user)
    }

    fun modifyPassword(currentPassword: String, newPassword: String, token: String): User {
        val user: User = findUser(token)
        if (!user.password.isSameAs(currentPassword)) {
            throw BadRequestException("authentication failed (wrong password).")
        }
        user.password = Password(newPassword)
        return repository.save(user)
    }
}