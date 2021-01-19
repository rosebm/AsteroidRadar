package com.rosalynbm.asteroidradar.business

import com.rosalynbm.asteroidradar.Utils
import com.rosalynbm.asteroidradar.models.PictureOfDay
import com.rosalynbm.asteroidradar.data.AsteroidDao
import com.rosalynbm.asteroidradar.models.Asteroid
import io.reactivex.Completable
import io.reactivex.Maybe
import timber.log.Timber
import java.time.LocalDate
import java.util.*

class AsteroidLocalDataSource(private val asteroidDao: AsteroidDao) {

    fun saveAsteroids(list: List<Asteroid>): Completable {
        return asteroidDao.insert(list)
            .doOnComplete {
                Timber.d("asteroid saved")
            }
    }

    fun getAsteroidsList(): Maybe<List<Asteroid>> {
        return asteroidDao.getAsteroids()
            .flatMap {
                if (it.isNotEmpty())
                    Maybe.just(it)
                else
                    Maybe.empty()
            }

    }

    fun getTodayAsteroidsList(): Maybe<List<Asteroid>> {
        return asteroidDao.getTodayAsteroids(Date())
            .flatMap {
                if (it.isNotEmpty())
                    Maybe.just(it)
                else
                    Maybe.empty()
            }
    }

    fun getWeekAsteroidsList(): Maybe<List<Asteroid>> {
        val todayLocalDate = LocalDate.now()
        val weekLocalDate = todayLocalDate.plusDays(7)
        val today = Utils.convertLocalDateToDate(todayLocalDate)
        val week = Utils.convertLocalDateToDate(weekLocalDate)

        return asteroidDao.getWeekAsteroids(today, week)
            .flatMap {
                if (it.isNotEmpty())
                    Maybe.just(it)
                else
                    Maybe.empty()
            }
    }

    fun saveImageOfDay(image: PictureOfDay): Completable {
      return asteroidDao.insertImageOfDay(image)
    }


    fun getImageOfDay(): Maybe<PictureOfDay> {
        return asteroidDao.getImageOfDay(Date())
    }
}