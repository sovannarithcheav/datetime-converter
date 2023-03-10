package kh.org.soramitsu.jpa_sort_util.config

import kh.org.soramitsu.jpa_sort_util.helper.Constants.AVAILABLE_SORT_KEYS
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
open class AvailableSortFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestWrapper = ContentCachingRequestWrapper(request)
        val responseWrapper = ContentCachingResponseWrapper(response)
        filterChain.doFilter(requestWrapper, responseWrapper)
        RequestContextHolder.currentRequestAttributes().getAttribute(AVAILABLE_SORT_KEYS, 0)?.let {
            responseWrapper.addHeader(AVAILABLE_SORT_KEYS, it.toString())
        }
        responseWrapper.copyBodyToResponse()
    }
}
