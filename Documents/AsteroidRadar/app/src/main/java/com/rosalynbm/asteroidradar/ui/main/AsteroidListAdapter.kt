package com.rosalynbm.asteroidradar.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rosalynbm.asteroidradar.R
import com.rosalynbm.asteroidradar.Utils
import com.rosalynbm.asteroidradar.models.Asteroid
import kotlinx.android.synthetic.main.list_item.view.*

class AsteroidListAdapter(private val listener: ListItemClickListener<Asteroid>)
    : ListAdapter<Asteroid, AsteroidListAdapter.AsteroidListViewHolder>(DIFF_CALLBACK) {

    companion object {

        val DIFF_CALLBACK: DiffUtil.ItemCallback<Asteroid> = object : DiffUtil.ItemCallback<Asteroid>() {
            // User properties may have changed if reloaded from the DB, but ID is fixed
            override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
                return oldItem.codename == newItem.codename
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return AsteroidListViewHolder(v)
    }

    override fun onBindViewHolder(holder: AsteroidListViewHolder, position: Int) {
        val asteroid = getItem(position)

        holder.name.text = asteroid.codename

        holder.date.text = Utils.getStringFromDate(asteroid.closeApproachDate)

        bindAsteroidStatusImage(holder.hazardIcon, asteroid.isPotentiallyHazardous)

        holder.itemView.setOnClickListener {
            listener.onListItemClicked(asteroid)
        }

    }

    inner class AsteroidListViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.name_text
        val date: TextView = view.date_text
        val hazardIcon: ImageView = view.hazard_indicator_image
    }

}