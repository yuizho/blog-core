package io.github.yuizho.blog.application.controllers

import io.github.yuizho.blog.domain.models.Loggedin
import io.github.yuizho.blog.application.services.LoginService
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/user")
class UserController(
        private val loginService: LoginService) {

    @PostMapping("/login")
    fun login(@NotNull @RequestParam(value="id") id: String,
              @NotNull @RequestParam(value="password") sentPass: String): Loggedin {
        return loginService.login(id, sentPass)
    }
}