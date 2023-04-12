package kh.org.sovannarith.datetime.config.custom

import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class CustomSerializableConverter<T> {
    abstract fun setupStdSerializer(): StdSerializer<T>
    abstract fun setupStdDeserializer(): StdDeserializer<T>
    fun <T> handleType(): Class<T> {
        val genericSuper = javaClass.genericSuperclass as ParameterizedType
        return genericSuper.actualTypeArguments[0] as Class<T>
    }
}