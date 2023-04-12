package kh.org.sovannarith.datetime.config

import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.module.SimpleSerializers
import kh.org.sovannarith.datetime.config.custom.CustomSerializableConverter
import kh.org.sovannarith.datetime.common.Constants.BEAN_PREFIX_NAME
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration

@Configuration("${BEAN_PREFIX_NAME}CustomSerializableModule")
open class CustomSerializableModule(private val ctx: ApplicationContext) : SimpleModule() {
    override fun setupModule(context: SetupContext) {
        val serializer = SimpleSerializers()
        val deserializer = SimpleDeserializers()
        val customSerializableConverters = ctx.getBeansOfType(CustomSerializableConverter::class.java)
        customSerializableConverters.map {
            val serializableConverter = it.value as CustomSerializableConverter
            serializer.addSerializer(serializableConverter.setupStdSerializer())
            deserializer.addDeserializer(serializableConverter.handleType(), serializableConverter.setupStdDeserializer())
        }
        context.addSerializers(serializer)
        context.addDeserializers(deserializer)
    }
}