package kh.org.sovannarith.jpa_sort_util.helper

import org.springframework.util.StringUtils

object Extension {
    fun notOnlyDots(source: String): Boolean {
        return StringUtils.hasText(source.replace(".", ""))
    }
}