package com.rosalynbm.asteroidradar.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rosalynbm.asteroidradar.data.DateConverter
import java.util.*

@Entity(tableName = "picture_tb")
@TypeConverters(DateConverter::class)
data class PictureOfDay(@PrimaryKey(autoGenerate = false)
                        val id: Int,
                        val mediaType: String,
                        val title: String,
                        val url: String,
                        val date: Date?
)