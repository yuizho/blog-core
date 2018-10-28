package io.github.yuizho.blog

import io.github.yuizho.blog.application.interceptors.AuthenticationInterceptor
import io.github.yuizho.blog.application.interceptors.CsrfFilterInterceptor
import io.github.yuizho.blog.application.services.UploadService
import io.github.yuizho.blog.domain.models.Password
import io.github.yuizho.blog.domain.models.User
import io.github.yuizho.blog.infrastructure.repositories.*
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
@EnableConfigurationProperties(LocalUploadProperties::class, UploadProperties::class)
class BlogCoreApplication {
    @Bean
    fun databaseInitializer(userRepository: UserRepository) = CommandLineRunner {
        val defaultUser = User(id = "default-user", password = Password("password"))
        if (userRepository.count() == 0L) {
            userRepository.save(defaultUser)
        }
    }
}

@Configuration
class WebMvcConfig(private val localUploadProperties: LocalUploadProperties,
                   private val loggeinRepository: LoggeinRepository): WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(CsrfFilterInterceptor())
                .addPathPatterns("/api/**")
        registry.addInterceptor(AuthenticationInterceptor(loggeinRepository))
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/user/login")
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry): Unit {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("file:///${localUploadProperties.path}")
    }

    @Bean
    fun uploadServiceFactory(environment: Environment,
                             applicationContext: ApplicationContext): UploadService =
        // default setting is LocalUpload (LocalUploadService class)
        // I followed this way!
        // https://stackoverflow.com/questions/7812745/spring-qualifier-and-property-placeholder
        applicationContext.getBean(environment.getProperty("upload.type") ?: "LocalUpload") as UploadService
}

fun main(args: Array<String>) {
    runApplication<BlogCoreApplication>(*args)
}
