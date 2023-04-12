package kh.org.sovannarith.datetime.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Import

@EnableConfigurationProperties(CommonUtilProperties::class)
@ConfigurationPropertiesScan("kh.org.sovannarith.*")
@Import(CustomSerializableModule::class)
open class DateTimeFormatConfiguration: ApplicationContextAware {
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        DATE_TIME_FORMAT = applicationContext.getBean(CommonUtilProperties::class.java).format
    }

    companion object {
        var DATE_TIME_FORMAT: CommonUtilProperties.Format = CommonUtilProperties.Format()
    }
}