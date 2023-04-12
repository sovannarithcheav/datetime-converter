package kh.org.sovannarith.datetime.config.custom.impl

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kh.org.sovannarith.datetime.config.custom.CustomSerializableConverter
import kh.org.sovannarith.datetime.helper.datetime.format
import kh.org.sovannarith.datetime.helper.datetime.toLocalDateTime
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
open class LocalDateTimeSerializer : CustomSerializableConverter<LocalDateTime>() {
    override fun setupStdSerializer(): StdSerializer<LocalDateTime> {
        return object : StdSerializer<LocalDateTime>(LocalDateTime::class.java) {
            override fun serialize(localDateTime: LocalDateTime?, gen: JsonGenerator, provider: SerializerProvider) {
                if (localDateTime != null ) {
                    gen.writeString(localDateTime.format())
                }
            }
        }
    }

    override fun setupStdDeserializer(): StdDeserializer<LocalDateTime> {
       return object: StdDeserializer<LocalDateTime>(LocalDateTime::class.java) {
            override fun deserialize(p: JsonParser?, ctxt: DeserializationContext): LocalDateTime? {
                if (p?.text == null || p.text == "") return null
                return p.text.toLocalDateTime()
            }
        }
    }
}