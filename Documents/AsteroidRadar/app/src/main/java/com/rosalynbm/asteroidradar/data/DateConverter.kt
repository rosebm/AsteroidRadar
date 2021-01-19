package com.rosalynbm.asteroidradar.data

import androidx.room.TypeConverter
import com.rosalynbm.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    @TypeConverter
    fun toDate(date: String): Date? {
        return SimpleDateFormat(API_QUERY_DATE_FORMAT).parse(date)
    }

    @TypeConverter
    fun toString(date: Date?): String? {
        val format = SimpleDateFormat(API_QUERY_DATE_FORMAT)
        return format.format(date)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
}