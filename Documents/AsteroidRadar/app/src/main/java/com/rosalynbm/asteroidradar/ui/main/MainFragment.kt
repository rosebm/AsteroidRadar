package com.rosalynbm.asteroidradar.ui.main

import android.app.Application
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.rosalynbm.asteroidradar.R
import com.rosalynbm.asteroidradar.business.AsteroidLocalDataSource
import com.rosalynbm.asteroidradar.business.AsteroidUseCase
import com.rosalynbm.asteroidradar.data.AppDatabase
import com.rosalynbm.asteroidradar.data.type.MediaType
import com.rosalynbm.asteroidradar.databinding.FragmentMainBinding
import com.rosalynbm.asteroidradar.worker.RefreshDataWorker
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    lateinit var repeatingRequest: PeriodicWorkRequest
    lateinit var w: WorkManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val asteroidLocalDataSource = AsteroidLocalDataSource(AppDatabase.getDatabase(requireContext()).getAsteroidDao())
        val repository = AsteroidUseCase(asteroidLocalDataSource)
        mainViewModel = MainViewModel(application = Application(), context = requireContext(), repository = repository)

        binding.viewModel = mainViewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAsteroidListAdapter()

        mainViewModel.getImageOfDay()
        setupRecurringWork()
        mainViewModel.getAsteroids()

        mainViewModel.onImageReady().observe(viewLifecycleOwner, Observer {
            //loadImage("https://apod.nasa.gov/apod/image/2012/GeminidMeteorsStePelle1024.jpg")
            if (it.mediaType == MediaType.IMAGE) {
                Timber.d("is an image")
                loadImage(it.url)
            }
            else {
                Timber.d("is a video")
                Toast.makeText(
                    requireContext(),
                    getString(R.string.main_cannot_display_video),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        mainViewModel.onAsteroidList().observe(viewLifecycleOwner, Observer {
            mainViewModel.getAsteroidListAdapter().submitList(it)
        })

        mainViewModel.navigateToDetailScreen().observe(viewLifecycleOwner, Observer {
            val bundle = bundleOf(getString(R.string.main_selected_asteroid) to it)
            Navigation.findNavController(asteroid_recycler).navigate(R.id.detailFragment, bundle)
        })

        w.getWorkInfoByIdLiveData(repeatingRequest.id).observe(viewLifecycleOwner,
            Observer {
                mainViewModel.getAsteroidsFromDB()
            })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_asteroids ->
                mainViewModel.getWeekAsteroids()

            R.id.show_today_asteroids ->
                mainViewModel.getTodayAsteroids()

            R.id.show_saved_asteroids ->
                mainViewModel.getAsteroidsFromDB()

        }
        return true
    }

    private fun loadImage(url: String) {
        Picasso.get()
            .load(url)
            .fit().centerCrop()
            .into(activity_main_image_of_the_day)
    }

    private fun initAsteroidListAdapter() {
        asteroid_recycler.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL))
        asteroid_recycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        asteroid_recycler.adapter = mainViewModel.getAsteroidListAdapter()
    }

    private fun setupRecurringWork() {
        // Define constraints to prevent work from occurring when there is no network access or
        // the phone is not charging.
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        repeatingRequest =
            PeriodicWorkRequestBuilder<RefreshDataWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

        w = WorkManager.getInstance(requireContext())

        w.enqueueUniquePeriodicWork(RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)

    }

}
