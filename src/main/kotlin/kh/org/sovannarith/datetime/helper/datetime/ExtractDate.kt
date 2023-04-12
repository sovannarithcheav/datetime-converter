package kh.org.sovannarith.datetime.helper.datetime

import java.time.LocalDate

object ExtractDate {
    fun extractDateRange(dateFrom: LocalDate, dateTo: LocalDate, modelName: String): Set<LocalDate> {
        if (dateFrom.isAfter(dateTo))
            throw RuntimeException("$modelName - dateTo($dateTo) must be greater than dateFrom($dateFrom)")

        val rangeDates = HashSet<LocalDate>()
        var date = dateFrom
        while (date <= dateTo) {
            rangeDates.add(date)
            date = date.plusDays(1)
        }
        return rangeDates
    }
}
