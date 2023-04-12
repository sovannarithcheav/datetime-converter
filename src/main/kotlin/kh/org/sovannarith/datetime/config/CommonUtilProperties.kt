package kh.org.sovannarith.datetime.config
import kh.org.sovannarith.datetime.common.Constants
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = true)
open class CommonUtilProperties(
    var format: Format = Format(),
    var shortIdLength: Int = 10
) {
    open class Format(
        var dateTime: String = Constants.DATETIME_FORMAT,
        var date: String = Constants.DATE_FORMAT,
        var time: String = Constants.TIME_FORMAT
    )
}