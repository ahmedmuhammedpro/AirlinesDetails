package com.ahmed.airlinesdetails.main_view.airline_searching

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ahmed.airlinesdetails.R
import com.ahmed.airlinesdetails.databinding.FragmentSearchBinding
import com.ahmed.airlinesmodel.AirlinesRepositoryImp
import com.ahmed.airlinesmodel.SearchingAirlineRepo
import com.ahmed.airlinesmodel.local.AirlineDatabase

class SearchFragment : Fragment() {

    private var id: String? = null
    private var term: String? = null
    private lateinit var databinding: FragmentSearchBinding
    private lateinit var searchingViewModel: SearchingViewModel
    private val searchingAirlineRepo : SearchingAirlineRepo by lazy {
        AirlinesRepositoryImp(AirlineDatabase.getDatabase(requireContext().applicationContext).airlineDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ID_PARAM)
            term = it.getString(TERM_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        searchingViewModel = ViewModelProvider(requireActivity(), SearchingViewModelFactory(searchingAirlineRepo)).get(SearchingViewModel::class.java)
        databinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        listenForAirline()
        setupClickListener()
        id?.let { searchingViewModel.getAirlineById(it) }
        term?.let { searchingViewModel.getAirLineByName(it) }

        return databinding.root
    }

    private fun listenForAirline() {
        searchingViewModel.loadingLiveData.observe(viewLifecycleOwner) {
            if (it) {
                databinding.loadingViewContainer.visibility = View.VISIBLE
                databinding.airlineView.visibility = View.GONE
                databinding.noDataContainer.visibility = View.GONE
            } else {
                databinding.loadingViewContainer.visibility = View.GONE
            }
        }

        searchingViewModel.airlineLiveDate.observe(viewLifecycleOwner) { airline ->
            if (airline != null) {
                databinding.airlineView.visibility = View.VISIBLE
                databinding.airlineView.findViewById<TextView>(R.id.airline_text_view).text = airline.name
            } else {
                databinding.noDataContainer.visibility = View.VISIBLE
                databinding.noDataTextView.text = getString(R.string.can_t_find_what_you_search)
            }
        }

        searchingViewModel.failingLiveData.observe(viewLifecycleOwner) {
            databinding.noDataContainer.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun setupClickListener() {
        databinding.goBack.setOnClickListener {
            findNavController().navigateUp()
        }

        databinding.tryAgain.setOnClickListener {
            id?.let { searchingViewModel.getAirlineById(it) }
            term?.let { searchingViewModel.getAirLineByName(it) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        const val ID_PARAM = "id_param"
        const val TERM_PARAM = "term_param"

        @JvmStatic
        fun newInstance(id: String? = null, term: String? = null) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_PARAM, id)
                    putString(TERM_PARAM, term)
                }
            }
    }
}