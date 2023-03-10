package kh.org.soramitsu.jpa_sort_util.annotation

import kotlin.reflect.KClass


@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class JpaSortMapping(
    val value: KClass<*>
)
