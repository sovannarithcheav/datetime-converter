package kh.org.sovannarith.jpa_sort_util.config

import kh.org.sovannarith.jpa_sort_util.annotation.JpaSortMapping
import kh.org.sovannarith.jpa_sort_util.annotation.JpaSortMappingResource
import kh.org.sovannarith.jpa_sort_util.config.SortOrderParser.Companion.parse
import kh.org.sovannarith.jpa_sort_util.helper.Constants.AVAILABLE_SORT_KEYS
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

@Configuration
open class JpaSortArgumentResolver(private val applicationContext: ApplicationContext) :
    SortHandlerMethodArgumentResolver() {

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
        if (jpaSortMapping != null) {
            val map = (jpaSortMapping as JpaSortMapping).value
            val resource = applicationContext.getBean(map.javaObjectType) as JpaSortMappingResource<*>
            val mapping = resource.getSource()
            RequestContextHolder.currentRequestAttributes()
                .setAttribute(AVAILABLE_SORT_KEYS, mapping.keys.joinToString { s -> s }, 0)
            val result =
                webRequest.getParameterValues(getSortParameter(parameter)) ?: return getDefaultFromAnnotationOrFallback(
                    parameter
                )
            val directionParameter = result.map {
                val splitChar = propertyDelimiter
                if (it.contains(splitChar)) {
                    if (it.split(splitChar).size != 2) throw IllegalArgumentException("Only supports a single direction")
                    val splits = it.split(splitChar)
                    if (Sort.Direction.fromOptionalString(splits[1]).isEmpty) throw IllegalArgumentException("Invalid direction")
                    val key = resource.getInternalSortKeyName(splits[0])?.let {  "$it$splitChar${splits[1]}"}
                    key ?: it
                } else resource.getInternalSortKeyName(it) ?: it
            }.distinct()
            return if (directionParameter.size == 1 && !StringUtils.hasText(directionParameter[0]))
                getDefaultFromAnnotationOrFallback(parameter)
            else parseParameterIntoSort(directionParameter)
        } else {
            return super.resolveArgument(parameter, mavContainer, webRequest, binderFactory)
        }
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