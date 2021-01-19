package com.rosalynbm.asteroidradar.api

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.io.IOException
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

class DateFormatJsonAdapter: JsonAdapter<Date>() {

    private val format = "yyyy-MM-dd'T'HH:mm:ss"

    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): Date? {
        val string = reader.nextString()
        return SimpleDateFormat(format, Locale.getDefault()).parse(string, ParsePosition(0))
    }

    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: Date?) {
        val date = SimpleDateFormat(format, Locale.getDefault()).format(value)
        writer.value(date)
    }
}