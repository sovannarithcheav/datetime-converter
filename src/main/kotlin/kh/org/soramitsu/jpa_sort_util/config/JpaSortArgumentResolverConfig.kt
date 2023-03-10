package kh.org.soramitsu.jpa_sort_util.config

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@Import(AvailableSortFilter::class, JpaSortArgumentResolver::class)
open class JpaSortArgumentResolverConfig {
    companion object {
        lateinit var APP_CONTEXT: ApplicationContext
    }

    @Bean
    open fun corsConfigurer(applicationContext: ApplicationContext): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
                APP_CONTEXT = applicationContext
                resolvers.add(JpaSortArgumentResolver(applicationContext))
                resolvers.add(PageableHandlerMethodArgumentResolver(JpaSortArgumentResolver(applicationContext)))
                super.addArgumentResolvers(resolvers)
            }
        }
    }
}
