package io.github.yuizho.blog.application.interceptors

import io.github.yuizho.blog.application.exceptions.BadRequestException
import io.github.yuizho.blog.application.exceptions.UnauthorizedException
import io.github.yuizho.blog.domain.models.Loggedin
import io.github.yuizho.blog.infrastructure.repositories.LoggeinRepository
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CsrfFilterInterceptor(): HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // when method is GET, don't need to check.
        // because this APP's GET API doesn't affect table data.
        if ("GET".equals(request.method, true)) {
            return true
        }
        // fetch
        val xRequestedWithHeader: String? = request.getHeader("X-Requested-With")
        // if accessToken is null, null will be returned
        // that's why this compare works fine
        if (xRequestedWithHeader?.isEmpty() != false) {
            throw BadRequestException("there is no required header.")
        }
        return true
    }
}