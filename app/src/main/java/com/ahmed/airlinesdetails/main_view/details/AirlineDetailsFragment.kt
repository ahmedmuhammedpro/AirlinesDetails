package com.ahmed.airlinesdetails.main_view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ahmed.airlinesdetails.R
import com.ahmed.airlinesdetails.databinding.FragmentAirlineDetailsBinding
import com.ahmed.airlinesdetails.model.entities.Airline

class AirlineDetailsFragment : Fragment() {

    lateinit var dataBinding: FragmentAirlineDetailsBinding
    private var airline: Airline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        airline = arguments?.getParcelable(AIRLINE_EXTRA)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_airline_details, container, false)
        dataBinding.lifecycleOwner = this
        airline?.let {
            dataBinding.airline = it
        }

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        return dataBinding.root
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