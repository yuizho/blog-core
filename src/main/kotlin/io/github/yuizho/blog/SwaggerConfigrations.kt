package io.github.yuizho.blog

import com.google.common.base.Predicate
import com.google.common.base.Predicates
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact


@Configuration
@EnableSwagger2
class SwaggerConfigrations {
    @Bean
    fun swaggerSpringPlugin(): Docket
        = Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(paths())
                .build()
                .apiInfo(apiInfo())

    private fun paths(): Predicate<String> {
        return Predicates.and(
                Predicates.not(Predicates.containsPattern("/hogehoge-view")),
                Predicates.or(
                        Predicates.containsPattern("/api/user/*"),
                        Predicates.containsPattern("/api/upload/*"),
                        Predicates.containsPattern("/api/tags/*"),
                        Predicates.containsPattern("/api/articles/*")))
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfo(
                "blog-core",
                "the specification of blog-core API",
                "0.0.1",
                "",
                Contact("yuizho", "https://github.com/yuizho", "yuizho3@gmail.com"),
                "",
                "",
                arrayListOf())
    }
}