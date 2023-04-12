package kh.org.sovannarith.datetime.helper.datetime

import kh.org.sovannarith.datetime.common.Constants.DATE_FORMAT_LIST
import kh.org.sovannarith.datetime.common.Constants.DATE_TIME_FORMAT_LIST
import kh.org.sovannarith.datetime.config.DateTimeFormatConfiguration.Companion.DATE_TIME_FORMAT
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.TemporalAdjusters
import java.util.*

fun LocalDateTime.format(pattern: String? = null): String {
    return this.format(DateTimeFormatter.ofPattern(pattern ?: DATE_TIME_FORMAT.dateTime))
}

fun LocalDate.format(pattern: String? = null): String {
    return this.format(DateTimeFormatter.ofPattern(pattern ?: DATE_TIME_FORMAT.date))
}

fun LocalTime.format(pattern: String? = null): String {
    return this.format(DateTimeFormatter.ofPattern(pattern ?: DATE_TIME_FORMAT.time))
}

fun Date.format(pattern: String? = null): String {
    return SimpleDateFormat(pattern ?: DATE_TIME_FORMAT.date).format(this)
}

fun String.toDate(pattern: String? = null): Date {
    return SimpleDateFormat(pattern ?: DATE_TIME_FORMAT.date).parse(this)
}

fun String.toLocalDate(pattern: String? = null): LocalDate {
    try {
        return LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern ?: DATE_TIME_FORMAT.date))
    } catch (ex: DateTimeParseException)  {
        DATE_FORMAT_LIST.map {
            try {
                return LocalDate.parse(this, DateTimeFormatter.ofPattern(it))
            } catch (ex: Exception) {
                return@map
            }
        }
        return this.toLocalDateTimeOrElse(pattern).toLocalDate()
    }
}

private fun String.toLocalDateOrElse(pattern: String? = null): LocalDate {
    val localDate: LocalDate? = try {
        LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern ?: DATE_TIME_FORMAT.date))
    } catch (ex: DateTimeParseException) {
        DATE_FORMAT_LIST.map {
            try {
                LocalDate.parse(this, DateTimeFormatter.ofPattern(it))
            } catch (ex: Exception) {
                return@map
            }
        }
        null
    }
    if (localDate == null) {
        val correctFormat = DateTimeFormatter.ofPattern(pattern ?: DATE_TIME_FORMAT.dateTime).format(LocalDate.now())
        throw RuntimeException("$this is invalid. Please enter a date in the format $correctFormat.")
    }
    return localDate
}

fun String.toLocalDateTime(pattern: String? = null): LocalDateTime {
    try {
        return LocalDateTime.parse(this, DateTimeFormatter.ofPattern(pattern ?: DATE_TIME_FORMAT.dateTime))
    } catch (ex: DateTimeParseException)  {
        DATE_TIME_FORMAT_LIST.map {
            try {
                return LocalDateTime.parse(this, DateTimeFormatter.ofPattern(it))
            } catch (ex: Exception) {
                return@map
            }
        }
        return this.toLocalDateOrElse(pattern).atTime(0, 0, 0)
    }
}

private fun String.toLocalDateTimeOrElse(pattern: String? = null): LocalDateTime {
    val localDateTime: LocalDateTime? = try {
        LocalDateTime.parse(this, DateTimeFormatter.ofPattern(pattern ?: DATE_TIME_FORMAT.dateTime))
    } catch (ex: DateTimeParseException) {
        DATE_TIME_FORMAT_LIST.map {
            try {
                return LocalDateTime.parse(this, DateTimeFormatter.ofPattern(it))
            } catch (ex: Exception) {
                return@map
            }
        }
        null
    }
    if (localDateTime == null) {
        val correctFormat = DateTimeFormatter.ofPattern(pattern ?: DATE_TIME_FORMAT.date).format(LocalDateTime.now())
        throw RuntimeException("$this is invalid. Please enter a date in the format $correctFormat.")
    }
    return localDateTime
}

fun String.toLocalTime(pattern: String? = null): LocalTime {
    return LocalTime.parse(this, DateTimeFormatter.ofPattern(pattern ?: DATE_TIME_FORMAT.time))
}

fun LocalDate.listDate(): Collection<LocalDate> {
    val start = this.with(TemporalAdjusters.firstDayOfMonth())
    val end = this.with(TemporalAdjusters.lastDayOfMonth())
    return from(start..end)
}

fun Interval.listDate(): Collection<LocalDate> {
    return from(this.start..this.end)
}

fun Collection<Interval>.listDate(): Collection<LocalDate> {
    val result = mutableSetOf<LocalDate>()
    this.map { result.addAll(from(it.start..it.end)) }
    return result
}

fun Interval.merge(other: Interval): List<Interval> {
    val merge = this.listDate() + other.listDate()
    return merge.distinct().merge()
}

fun Collection<Interval>.merge(other: Collection<Interval>): Collection<Interval> {
    val thisDates = this.listDate() + other.listDate()
    return thisDates.distinct().merge()
}

fun from(
    range: ClosedRange<LocalDate>,
    fromFirstDay: Boolean = false,
    toLastDay: Boolean = false
): MutableCollection<LocalDate> {
    val start: LocalDate = if (fromFirstDay) range.start.with(TemporalAdjusters.firstDayOfMonth()) else range.start
    val end: LocalDate =
        if (toLastDay) range.endInclusive.with(TemporalAdjusters.lastDayOfMonth()) else range.endInclusive
    var startDate = LocalDate.of(start.year, start.month, start.dayOfMonth).minusDays(1)
    val dates = mutableListOf<LocalDate>()
    while (startDate < end) {
        startDate = startDate.plusDays(1)
        dates.add(startDate)
    }
    return dates
}

fun List<LocalDate>.merge(): List<Interval> {
    val groupByYear = this.distinct().sorted().groupBy { it.year }
    val result = mutableListOf<Interval>()
    groupByYear.values.map { dates ->
        var range = Interval(dates.first(), dates.first())
        val ranges = mutableListOf<Interval>()
        dates.map { date ->
            if (dates.size == 1) {
                ranges.add(range)
            } else if (range.end.plusDays(1) == date) {
                range.end = range.end.plusDays(1)
                range.range++
                ranges.add(range)
            } else {
                range = Interval(date, date)
                ranges.add(range)
            }
        }
        result.addAll(ranges.distinct())
    }
    return result
}

fun Long.fromNow(): LocalDateTime {
    return Date(System.currentTimeMillis() + this * 1000).toLocalDateTime()
}

fun Date.toLocalDateTime(): LocalDateTime {
    return this.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
}
