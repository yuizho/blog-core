package io.github.yuizho.blog.application.services

import io.github.yuizho.blog.application.exceptions.UnauthorizedException
import io.github.yuizho.blog.domain.models.Loggedin
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
        val user: User = repository.findById(id)
                .orElseThrow { UnauthorizedException("id or password is wrong.") }
        if (!user.password.isSameAs(sentPass)) { throw UnauthorizedException("id or password is wrong.") }
        loggeinRepository.deleteByUser(user.id)
        return loggeinRepository.save(Loggedin(user = user))
    }
}