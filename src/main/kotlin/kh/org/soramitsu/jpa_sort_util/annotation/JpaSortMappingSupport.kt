package kh.org.soramitsu.jpa_sort_util.annotation

import kh.org.soramitsu.jpa_sort_util.config.JpaSortArgumentResolverConfig.Companion.APP_CONTEXT
import java.lang.reflect.ParameterizedType

abstract class  JpaSortMappingSupport<T>: JpaSortMappingResource<T> {
    override fun getSource(): MutableMap<String, String> {
        val javaName = (this::class.java.genericInterfaces.first() as ParameterizedType).actualTypeArguments.first().typeName
        return APP_CONTEXT.classLoader!!.loadClass(javaName).declaredFields.associate {
            it.name to "_"
        }.toMutableMap()
    }
}