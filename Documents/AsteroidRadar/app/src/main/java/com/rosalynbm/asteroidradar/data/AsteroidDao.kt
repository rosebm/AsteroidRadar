package com.rosalynbm.asteroidradar.data

import androidx.room.*
import com.rosalynbm.asteroidradar.models.PictureOfDay
import com.rosalynbm.asteroidradar.models.Asteroid
import io.reactivex.Completable
import io.reactivex.Maybe
import java.util.*

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(asteroid: List<Asteroid>): Completable

    @Transaction
    @Query("SELECT * from asteroid_tb order by closeApproachDate asc")
    fun getAsteroids(): Maybe<List<Asteroid>>

    @Transaction
    @Query("SELECT * from asteroid_tb where closeApproachDate = :date order by closeApproachDate asc")
    fun getTodayAsteroids(date: Date): Maybe<List<Asteroid>>

    @Transaction
    @Query("SELECT * from asteroid_tb where closeApproachDate between :dateFrom and :dateTo order by closeApproachDate asc")
    fun getWeekAsteroids(dateFrom: Date, dateTo: Date): Maybe<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImageOfDay(image: PictureOfDay): Completable

    @Transaction
    @Query("SELECT * from picture_tb where date = :date")
    fun getImageOfDay(date: Date): Maybe<PictureOfDay>

    @Transaction
    @Query("DELETE from picture_tb")
    fun clearImageTable(): Completable
}
