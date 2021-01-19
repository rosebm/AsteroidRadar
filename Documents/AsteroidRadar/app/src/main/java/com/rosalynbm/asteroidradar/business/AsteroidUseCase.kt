package com.rosalynbm.asteroidradar.business

import com.rosalynbm.asteroidradar.Constants
import com.rosalynbm.asteroidradar.api.AsteroidRemoteDataSource
import com.rosalynbm.asteroidradar.api.parseAsteroidsJsonResult
import com.rosalynbm.asteroidradar.models.Asteroid
import com.rosalynbm.asteroidradar.models.PictureOfDay
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import kotlin.collections.ArrayList

class AsteroidUseCase(private val asteroidLocalDataSource: AsteroidLocalDataSource) {

    private val asteroidRemoteDataSource = AsteroidRemoteDataSource()

    /**
     * This is for regular call, no workmanager involved
     */
    fun getAsteroids(api_key: String): Single<List<Asteroid>> {
        return asteroidLocalDataSource.getAsteroidsList()
            .switchIfEmpty(getAsteroidsFromRemote(api_key))
            .subscribeOn(Schedulers.io())
    }

    fun getAsteroidsFromRemote(api_key: String): Single<ArrayList<Asteroid>> {
        return asteroidRemoteDataSource.getAsteroids(api_key)
            .flatMap {
                val jsonObject = JSONObject(it)
                val list = parseAsteroidsJsonResult(jsonObject)
                asteroidLocalDataSource.saveAsteroids(list)
                    .toSingle { list }
            }
            .subscribeOn(Schedulers.io())
    }

    fun getImageOfDay(): Single<PictureOfDay> {
        return asteroidLocalDataSource.getImageOfDay()
            .switchIfEmpty(fetchImageFromRemote())
            .subscribeOn(Schedulers.io())
    }

    private fun fetchImageFromRemote(): Single<PictureOfDay> {
        return asteroidRemoteDataSource.getImageOfTheDay(Constants.API_KEY)
            .flatMap {
                asteroidLocalDataSource.saveImageOfDay(it.convertToPictureOfDay())
                    .toSingle { it.convertToPictureOfDay() }
            }
            .subscribeOn(Schedulers.io())
    }

    fun getAsteroidsFromDB(): Maybe<List<Asteroid>> {
        return asteroidLocalDataSource.getAsteroidsList()
            .subscribeOn(Schedulers.io())

    }

    fun getAsteroidsFromToday(): Maybe<List<Asteroid>> {
        return asteroidLocalDataSource.getTodayAsteroidsList()
            .subscribeOn(Schedulers.io())
    }

    fun getAsteroidsFromWeek(): Maybe<List<Asteroid>> {
        return asteroidLocalDataSource.getWeekAsteroidsList()
            .subscribeOn(Schedulers.io())
    }

}