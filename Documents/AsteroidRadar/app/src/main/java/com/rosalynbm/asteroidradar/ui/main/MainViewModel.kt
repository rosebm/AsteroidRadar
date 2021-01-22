package com.rosalynbm.asteroidradar.ui.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.rosalynbm.asteroidradar.Constants
import com.rosalynbm.asteroidradar.R
import com.rosalynbm.asteroidradar.models.PictureOfDay
import com.rosalynbm.asteroidradar.business.AsteroidUseCase
import com.rosalynbm.asteroidradar.models.Asteroid
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MainViewModel(application: Application,
                    private val context: Context,
                    private val repository: AsteroidUseCase): AndroidViewModel(application),
    ListItemClickListener<Asteroid> {

    private val asteroidListLiveData = MutableLiveData<List<Asteroid>>()
    private val navigateToScreenLiveData = MutableLiveData<Asteroid>()
    private val imageReadyLiveData = MutableLiveData<PictureOfDay>()

    fun onAsteroidList(): LiveData<List<Asteroid>> = asteroidListLiveData
    fun navigateToDetailScreen(): LiveData<Asteroid> = navigateToScreenLiveData
    fun onImageReady(): LiveData<PictureOfDay> = imageReadyLiveData
    private val asteroidListAdapter: AsteroidListAdapter = AsteroidListAdapter(this)

    fun getAsteroidListAdapter() = asteroidListAdapter

    fun getImageOfDay() {
        CompositeDisposable(
            repository.getImageOfDay()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    imageReadyLiveData.postValue(it)
                    Timber.d(context.getString(R.string.main_got_image_day))
                },{
                    Timber.e("Error fetching asteroids list: ${it.message}")
                })
        )
    }

    override fun onListItemClicked(item: Asteroid) {
        navigateToScreenLiveData.value = item
    }

    fun getAsteroids() {
        CompositeDisposable(
                repository.getAsteroids(Constants.API_KEY)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            asteroidListLiveData.postValue(it)
                            getImageOfDay()
                            Timber.d(context.getString(R.string.main_got_asteroids))
                        },{
                            Timber.e(context.getString(R.string.main_error_fetching_list))
                        })
        )
    }

    fun getAsteroidsFromRemote() {
        CompositeDisposable(
            repository.getAsteroidsFromRemote(Constants.API_KEY)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    asteroidListLiveData.postValue(it)
                    Timber.d(context.getString(R.string.main_got_asteroids))
                },{
                    Timber.e(context.getString(R.string.main_error_fetching_list))
                })
        )
    }

    fun getAsteroidsFromDB() {
        CompositeDisposable(
            repository.getAsteroidsFromDB()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    asteroidListLiveData.postValue(it)
                    getImageOfDay()
                    Timber.d( "Got asteroids DB")
                },{
                    Timber.d("Error fetching asteroids from db: ${it.message}")
                })
        )
    }

    fun getTodayAsteroids() {
        CompositeDisposable(
            repository.getAsteroidsFromToday()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Got today's asteroids")
                    asteroidListLiveData.postValue(it)
                },{
                    Timber.d("Error fetching today's asteroids list: ${it.message}")
                })
        )
    }

    fun getWeekAsteroids() {
        CompositeDisposable(
            repository.getAsteroidsFromWeek()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d( "Got week's asteroids")
                    asteroidListLiveData.postValue(it)
                },{
                    Timber.d("Error fetching week's asteroids list: ${it.message}")
                })
        )
    }

}

interface ListItemClickListener<T> {
    fun onListItemClicked(item: T)
}