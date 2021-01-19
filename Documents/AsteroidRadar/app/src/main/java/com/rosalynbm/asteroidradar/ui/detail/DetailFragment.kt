package com.rosalynbm.asteroidradar.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.rosalynbm.asteroidradar.R
import com.rosalynbm.asteroidradar.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val asteroid = arguments?.let { DetailFragmentArgs.fromBundle(it).selectedAsteroid }

        asteroid?.absoluteMagnitude?.let {
            bindTextViewToAstronomicalUnit(binding.absoluteMagnitude,
                it
            )
        }

        asteroid?.closeApproachDate?.let {
            bindTextViewToApproachDate(binding.closeApproachDate, it)
        }

        asteroid?.estimatedDiameter?.let {
            bindTextViewToKmUnit(binding.estimatedDiameter, it)
        }

        asteroid?.relativeVelocity?.let {
            bindTextViewToDisplayVelocity(binding.relativeVelocity, it)
        }

        asteroid?.distanceFromEarth?.let {

        }

        binding.asteroid = asteroid

        binding.helpButton.setOnClickListener {
            displayAstronomicalUnitExplanationDialog()
        }

        return binding.root
    }

    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)

        val dialog = builder.create()
        dialog.findViewById<TextView>(android.R.id.message)?.textSize = 40F
        dialog.show()

    }
}
