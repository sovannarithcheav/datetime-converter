package kh.org.sovannarith.datetime.common

object Constants {
    const val ISO_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    const val DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss"
    const val DATE_FORMAT = "dd/MM/yyyy"
    const val TIME_FORMAT = "HH:mm"
    const val DEFAULT_PHONE_REGION = "KH"
    const val BEAN_PREFIX_NAME="dateTimeConverter"

    val DATE_FORMAT_LIST = listOf(
        "dd/MMM/yyyy",
        "dd-MMM-yyyy",
        "dd/MM/yyyy",
        "dd-MM-yyyy",
        "yyyy-MM-dd",
        "yyyy/MM/dd",
        "yyyy-MMM-dd",
        "yyyy/MMM/dd"
    )

    val DATE_TIME_FORMAT_LIST = listOf(
        "dd/MMM/yyyy HH:mm:ss",
        "dd-MMM-yyyy HH:mm:ss",
        "dd/MM/yyyy HH:mm:ss",
        "dd-MM-yyyy HH:mm:ss",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy/MM/dd HH:mm:ss",
        "yyyy-MMM-dd HH:mm:ss",
        "yyyy/MMM/dd HH:mm:ss",
        "dd-MM-yyyy'T'HH:mm:ss",
        "dd/MM/yyyy'T'HH:mm:ss",
        "dd-MMM-yyyy'T'HH:mm:ss",
        "dd/MMM/yyyy'T'HH:mm:ss",
    )
}