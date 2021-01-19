package com.rosalynbm.asteroidradar.worker

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.rosalynbm.asteroidradar.Constants
import com.rosalynbm.asteroidradar.business.AsteroidLocalDataSource
import com.rosalynbm.asteroidradar.business.AsteroidUseCase
import com.rosalynbm.asteroidradar.data.AppDatabase
import io.reactivex.Single

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    RxWorker (appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override fun createWork(): Single<Result> {
        val asteroidLocalDataSource = AsteroidLocalDataSource(
            AppDatabase.getDatabase(applicationContext).getAsteroidDao())
        val repository = AsteroidUseCase(asteroidLocalDataSource)

        return repository.getAsteroidsFromRemote(Constants.API_KEY)
            .map {
                Result.success()
            }
            .onErrorReturn {
                Result.failure()
            }
    }

}