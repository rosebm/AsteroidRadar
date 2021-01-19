package com.rosalynbm.asteroidradar.api

import com.rosalynbm.asteroidradar.api.response.JSONImageDay
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("neo/rest/v1/feed")
    fun getAsteroids(@Query("api_key")
                     apiKey: String): Single<String>

    @GET("planetary/apod")
    fun getImageOfTheDay(@Query("api_key")
                         apiKey: String): Single<JSONImageDay>

    /*@GET("neo/rest/v1/neo/2137158?")
    fun getAsteroid()*/
}