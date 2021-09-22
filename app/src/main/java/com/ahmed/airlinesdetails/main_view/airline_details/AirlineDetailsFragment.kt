package com.ahmed.airlinesdetails.main_view.airline_details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ahmed.airlinesdetails.R
import com.ahmed.airlinesdetails.databinding.FragmentAirlineDetailsBinding
import com.ahmed.airlinesdetails.main_view.MainActivity
import com.ahmed.airlinesmodel.entities.Airline

class AirlineDetailsFragment : Fragment() {

    lateinit var dataBinding: FragmentAirlineDetailsBinding
    private var airline: Airline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        airline = arguments?.getParcelable(AIRLINE_EXTRA)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_airline_details, container, false)
        dataBinding.lifecycleOwner = this
        airline?.let {
            dataBinding.airline = it
        }

        setHasOptionsMenu(true)

        dataBinding.visitButton.setOnClickListener {
            var url = airline?.website
            if (!url.isNullOrEmpty()) {
                if (!url.startsWith("http", ignoreCase = true)) {
                    url = "http://$url"
                }
                val visitIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(visitIntent)
            }
        }

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        (activity as MainActivity).setupToolbarTitle(getString(R.string.airline_title))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        const val AIRLINE_EXTRA = "details.airline_extra"

        @JvmStatic
        fun newInstance(airline: Airline) {
            AirlineDetailsFragment().apply {
                arguments?.putParcelable(AIRLINE_EXTRA, airline)
            }
        }
    }
}