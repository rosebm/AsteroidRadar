package com.rosalynbm.asteroidradar.api.response

import com.rosalynbm.asteroidradar.models.PictureOfDay
import com.squareup.moshi.Json
import java.util.*

class JSONImageDay(@Json(name = "date")
                   val date: Date?,
                   val explanation: String,
                   @Json(name = "media_type")
                   val mediaType: String,
                   val title: String,
                   val url: String) {

    fun convertToPictureOfDay(): PictureOfDay {
        return PictureOfDay(Random().nextInt(), mediaType, title, url, date ?: Date())
    }
}