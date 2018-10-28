package io.github.yuizho.blog.application.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException(message: String): RuntimeException(message) {
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException(message: String): RuntimeException(message) {
}

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnauthorizedException(message: String): RuntimeException(message) {
}

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class SystemException(message: String): RuntimeException() {
}