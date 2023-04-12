package kh.org.sovannarith.datetime.config.custom.impl

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kh.org.sovannarith.datetime.config.custom.CustomSerializableConverter
import kh.org.sovannarith.datetime.helper.datetime.format
import kh.org.sovannarith.datetime.helper.datetime.toLocalDate
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
open class LocalDateSerializer : CustomSerializableConverter<LocalDate>() {
    override fun setupStdSerializer(): StdSerializer<LocalDate> {
        return object : StdSerializer<LocalDate>(LocalDate::class.java) {
            override fun serialize(localDate: LocalDate?, gen: JsonGenerator, provider: SerializerProvider) {
                if (localDate != null ) {
                    gen.writeString(localDate.format())
                }
            }
        }
    }

    override fun setupStdDeserializer(): StdDeserializer<LocalDate> {
       return object: StdDeserializer<LocalDate>(LocalDate::class.java) {
            override fun deserialize(p: JsonParser?, ctxt: DeserializationContext): LocalDate? {
                if (p?.text == null || p.text == "") return null
                return p.text.toLocalDate()
            }
        }
    }
}