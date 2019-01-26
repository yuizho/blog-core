package io.github.yuizho.blog.application.controllers

import io.github.yuizho.blog.application.services.LoginService
import io.github.yuizho.blog.domain.models.Loggedin
import io.github.yuizho.blog.domain.models.User
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

    @PostMapping("/logout")
    fun logout(@NotNull @RequestHeader(value = "Authorization") token: String): Unit {
        loginService.logout(token)
    }

    @PutMapping("")
    fun modifyUser(@RequestParam(value="id") id: String?,
                 @RequestParam(value="password") sentPass: String?,
                 @NotNull @RequestHeader(value = "Authorization") token: String): User {
        return loginService.modifyUser(id, sentPass, token)
    }

    @PutMapping("password")
    fun modifyPassword(@RequestParam(value="current_password") currentPassword: String,
                   @RequestParam(value="new_password") newPassword: String,
                   @NotNull @RequestHeader(value = "Authorization") token: String): User {
        return loginService.modifyPassword(currentPassword, newPassword, token)
    }
}