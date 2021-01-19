package com.rosalynbm.asteroidradar.api

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.moshi.Moshi
import com.rosalynbm.asteroidradar.Constants
import com.rosalynbm.asteroidradar.api.response.JSONImageDay
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

class AsteroidRemoteDataSource: ApiService {

    private val api: ApiService = buildApi()

    private fun buildApi(): ApiService {

        // Moshi converts JSON into Kotlin objects
        val moshi = Moshi.Builder()
            .add(Date::class.java, DateFormatJsonAdapter().nullSafe())
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            // For when a Json response cannot be parsed directly using Moshi.
            // Order matters, scalar must come first
            .addConverterFactory(ScalarsConverterFactory.create())
            // So Moshi annotations can work properly with Kotlin
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(ApiService::class.java)
    }


    override fun getAsteroids(api_key: String
    ): Single<String> {
        return api.getAsteroids(api_key)
    }

    override fun getImageOfTheDay(apiKey: String): Single<JSONImageDay> {
        return api.getImageOfTheDay(apiKey)
    }
}