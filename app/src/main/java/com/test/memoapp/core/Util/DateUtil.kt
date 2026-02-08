package com.test.memoapp.core.Util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


object DateFormatUtils {
    fun convertLongToString(time: Long, type: DateConvertType): String {
        val instant = Instant.ofEpochMilli(time)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val result = dateTime.format(DateTimeFormatter.ofPattern(type.pattern))
        return result
    }

    fun timeFormatPattern() = DateTimeFormatter.ofPattern("HH : mm");
    fun DateFormatPattern() = DateTimeFormatter.ofPattern("yyyy-mm-dd");

    fun convertLongToDate(dateMillis: Long): LocalDate {
        val date = Instant.ofEpochMilli(dateMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        return date
    }

    fun convertLongToTime(dateMillis: Long) : LocalTime {
        val time = Instant.ofEpochMilli(dateMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
        return time
    }

    fun convertLocalDateTimeToLong(dateTime: LocalDateTime): Long {
        return dateTime.atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    fun convertLocalTimeToLong(firstDay: LocalDate) : Long {
        return firstDay.atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }
}

enum class DateConvertType(val pattern: String) {
    DEFAULT("yyyy-MM-dd"), KR("yyyy년 MM월 dd일") , MonthDate("MM-dd") , time("H:mm"), DATETIME("yyyy-MM-dd \n HH:mm")
}