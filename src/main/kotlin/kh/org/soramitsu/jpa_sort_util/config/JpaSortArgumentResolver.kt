package kh.org.soramitsu.jpa_sort_util.config

import kh.org.soramitsu.jpa_sort_util.helper.Constants.AVAILABLE_SORT_KEYS
import kh.org.soramitsu.jpa_sort_util.annotation.JpaSortMapping
import kh.org.soramitsu.jpa_sort_util.annotation.JpaSortMappingResource
import kh.org.soramitsu.jpa_sort_util.config.SortOrderParser.Companion.parse
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.data.domain.Sort
import org.springframework.data.web.SortHandlerMethodArgumentResolver
import org.springframework.util.StringUtils
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.method.support.ModelAndViewContainer
import java.util.*

@Configuration
open class JpaSortArgumentResolver(private val applicationContext: ApplicationContext) : SortHandlerMethodArgumentResolver() {

    override fun setPropertyDelimiter(propertyDelimiter: String) {
        super.setPropertyDelimiter(getPropertyDelimiter())
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return Sort::class.java == parameter.parameterType;
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Sort {
        val jpaSortMapping = parameter.method?.annotations?.firstOrNull { it is JpaSortMapping }
        val directionParameter = if (jpaSortMapping != null) {
            val map = (jpaSortMapping as JpaSortMapping).value
            val resource = applicationContext.getBean(map.javaObjectType) as JpaSortMappingResource
            val mapping = resource.getSource()
            RequestContextHolder.currentRequestAttributes()
                .setAttribute(AVAILABLE_SORT_KEYS, mapping.keys.joinToString { s -> s }, 0)
            val result =
                webRequest.getParameterValues(getSortParameter(parameter)) ?: return getDefaultFromAnnotationOrFallback(
                    parameter
                )
            result.map {
                if (it.contains(propertyDelimiter)) {
                    val splits = it.split(propertyDelimiter)
                    (resource.getInternalSortKeyName(splits[0]) ?: it) + propertyDelimiter + splits[1]
                } else resource.getInternalSortKeyName(it) ?: it
            }
        } else return getDefaultFromAnnotationOrFallback(parameter)
        return if (directionParameter.size == 1 && !StringUtils.hasText(directionParameter[0])) {
            getDefaultFromAnnotationOrFallback(parameter)
        } else parseParameterIntoSort(directionParameter)
    }

    open fun parseParameterIntoSort(source: List<String>): Sort {
        val allOrders: MutableList<Sort.Order> = ArrayList()
        for (part in source) {
            parse(part, this.propertyDelimiter)
                .parseIgnoreCase()
                .parseDirection()
                .forEachOrder { e: Sort.Order? ->
                    allOrders.add(
                        e!!
                    )
                }
        }
        return if (allOrders.isEmpty()) Sort.unsorted() else Sort.by(allOrders)
    }
}