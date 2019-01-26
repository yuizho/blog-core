package io.github.yuizho.blog.application.interceptors

import io.github.yuizho.blog.application.exceptions.BadRequestException
import io.github.yuizho.blog.application.exceptions.UnauthorizedException
import io.github.yuizho.blog.domain.models.Loggedin
import io.github.yuizho.blog.domain.models.Token
import io.github.yuizho.blog.infrastructure.repositories.LoggeinRepository
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationInterceptor(private val loggeinRepository: LoggeinRepository): HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // when method is GET, don't need to authenticate.
        // because this APP's GET API doesn't affect table data.
        // and also OPTIONS is also don't need to check.
        // because it's just for pre flight request of CORS.
        // https://developer.mozilla.org/ja/docs/Web/HTTP/Methods/OPTIONS
        if ("GET".equals(request.method, true) || "OPTIONS".equals(request.method, true)) {
            return true;
        }
        // fetch Authorization header to get access token
        val accessTokenWithScheme: String? = request.getHeader("Authorization")
        // if accessToken is null, null will be returned
        // that's why this compare works fine
        if (accessTokenWithScheme?.isEmpty() != false) {
            throw BadRequestException("there is no proper token.")
        }
        val accessToken = Token(accessTokenWithScheme)
        if (!accessToken.isExpectedScheme()) {
            throw BadRequestException("unexpected Authorization scheme: ${accessToken.scheme}.")
        }
        val loggedin: Loggedin = loggeinRepository.findByToken(accessToken)
                ?: throw UnauthorizedException("Invalid token.")
        if (loggedin.isExpired()) {
            throw UnauthorizedException("the token is expired.")
        }
        return true
    }
}