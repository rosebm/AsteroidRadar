package com.rosalynbm.asteroidradar

import com.rosalynbm.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

object Utils {

    private fun getFormat(): SimpleDateFormat {
        return SimpleDateFormat(API_QUERY_DATE_FORMAT)
    }

    fun getDateFromString(date: String): Date = getFormat().parse(date)

    @JvmStatic
    fun getStringFromDate(date: Date) : String {
        val format = getFormat()
        return format.format(date)
    }

    fun convertLocalDateToDate(localDate: LocalDate): Date {
        val zoneId = ZoneId.systemDefault()

        return Date.from(localDate.atStartOfDay(zoneId).toInstant())
    }

}