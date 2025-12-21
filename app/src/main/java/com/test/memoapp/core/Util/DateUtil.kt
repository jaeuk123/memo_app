package com.test.memoapp.core.Util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.IsoFields


object DateUtils {
    fun convertLongToString(time : Long,type : DateConvertType): String {
        val instant = Instant.ofEpochMilli(time)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val result = dateTime.format(DateTimeFormatter.ofPattern(type.pattern))
        return result
    }
}

enum class DateConvertType(val pattern:String) {
    DEFAULT("yyyy-MM-dd") , KR("yyyy년 MM월 dd일")
}