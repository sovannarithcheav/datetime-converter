package kh.org.sovannarith.jpa_sort_util.annotation

import kh.org.sovannarith.jpa_sort_util.config.JpaSortArgumentResolverConfig
import java.lang.reflect.ParameterizedType

interface JpaSortMappingResource<T> {

    fun getSource(): MutableMap<String, String> = getDefaultPropertiesSorting()

    fun getInternalSortKeyName(name: String): String? {
        val key = this.getSource()[name]
        return if (key == "_") name else key
    }

    fun getDefaultPropertiesSorting(): MutableMap<String, String> {
        val javaName = (this::class.java.genericInterfaces.first() as ParameterizedType).actualTypeArguments.first().typeName
        return JpaSortArgumentResolverConfig.APP_CONTEXT.classLoader!!.loadClass(javaName).declaredFields.associate {
            it.name to "_"
        }.toMutableMap()
    }
}