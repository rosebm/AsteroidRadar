package com.rosalynbm.asteroidradar.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rosalynbm.asteroidradar.data.DateConverter
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "asteroid_tb")
@TypeConverters(DateConverter::class)
data class Asteroid(@PrimaryKey(autoGenerate = false)
                    val id: Long,
                    val codename: String,
                    val closeApproachDate: Date,
                    val absoluteMagnitude: Double,
                    val estimatedDiameter: Double,
                    val relativeVelocity: Double,
                    val distanceFromEarth: Double,
                    val isPotentiallyHazardous: Boolean) : Parcelable