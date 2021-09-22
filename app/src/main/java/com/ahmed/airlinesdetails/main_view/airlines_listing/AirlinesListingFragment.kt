package com.ahmed.airlinesdetails.main_view.airlines_listing

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ahmed.airlinesdetails.R
import com.ahmed.airlinesdetails.databinding.FragmentAirlinesListingBinding
import com.ahmed.airlinesdetails.main_view.FragmentDestination
import com.ahmed.airlinesdetails.main_view.MainActivity
import com.ahmed.airlinesdetails.main_view.MainViewModel
import com.ahmed.airlinesdetails.main_view.airline_details.AirlineDetailsFragment
import com.ahmed.airlinesdetails.main_view.airline_searching.SearchFragment
import com.ahmed.airlinesmodel.AirlinesRepository
import com.ahmed.airlinesmodel.AirlinesRepositoryImp
import com.ahmed.airlinesmodel.entities.Airline
import com.ahmed.airlinesmodel.local.AirlineDatabase
import com.google.android.material.snackbar.Snackbar

class AirlinesListFragment : Fragment(), OnItemClick {

    private lateinit var airlinesViewModel: AirlineListingViewModel
    private lateinit var databinding: FragmentAirlinesListingBinding
    private lateinit var airlinesAdapter: AirlinesAdapter
    private lateinit var mainViewModel: MainViewModel
    private var mRootView: View? = null
    private val airlinesRepo: AirlinesRepository by lazy {
        AirlinesRepositoryImp(AirlineDatabase.getDatabase(requireContext().applicationContext).airlineDao)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (mRootView == null) {
            databinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_airlines_listing,
                container,
                false
            )
            airlinesViewModel =
                ViewModelProvider(requireActivity(), AirlineListingViewModelFactory(airlinesRepo))
                    .get(AirlineListingViewModel::class.java)
            mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
            databinding.viewModel = airlinesViewModel
            databinding.lifecycleOwner = this
            airlinesAdapter = AirlinesAdapter(arrayListOf(), this)
            databinding.airlinesRecyclerView.adapter = airlinesAdapter

            airlinesViewModel.getAirlines()
            listenForAirlinesResponse()

            setUpViewListener()
            mRootView = databinding.root
        } else {
            (activity as MainActivity).setupToolbarTitle(getString(R.string.airlines_title))
        }

        return mRootView
    }

    private fun listenForAirlinesResponse() {
        airlinesViewModel.airlinesLiveDate.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                databinding.mainContainer.visibility = View.VISIBLE
                airlinesAdapter.items.clear()
                airlinesAdapter.items.addAll(it)
                airlinesAdapter.notifyDataSetChanged()
            } else {
                databinding.mainContainer.visibility = View.GONE
                databinding.errorViewContainer.visibility = View.VISIBLE
            }
        }

        airlinesViewModel.loadingLiveData.observe(viewLifecycleOwner) {
            databinding.loadingViewContainer.visibility = if (it) View.VISIBLE else View.GONE
        }

        airlinesViewModel.failingLiveData.observe(viewLifecycleOwner) {
            showSnackBar(getString(R.string.general_error))
        }
    }

    private fun setUpViewListener() {

        databinding.tryAgain.setOnClickListener {
            databinding.errorViewContainer.visibility = View.GONE
            databinding.loadingViewContainer.visibility = View.VISIBLE
            airlinesViewModel.getAirlines()
        }

        databinding.searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchTerm = databinding.searchEditText.text.toString()
                if (searchTerm.isNotEmpty()) {
                    databinding.mainContainer.visibility = View.GONE
                    databinding.searchEditText.setText("")
                    searchByNameOrId(searchTerm)
                    hideKeyboard()
                }
                return@setOnEditorActionListener true
            }

            false
        }

        databinding.searchButton.setOnClickListener {
            val searchTerm = databinding.searchEditText.text.toString()
            databinding.searchEditText.setText("")
            if (searchTerm.isNotEmpty()) {
                searchByNameOrId(searchTerm)
                hideKeyboard()
            }
        }

        databinding.tryAgain.setOnClickListener {
            databinding.errorViewContainer.visibility = View.GONE
            airlinesViewModel.getAirlines()
        }

        databinding.addAirlineButton.setOnClickListener {
            mainViewModel.setNewDestination(FragmentDestination(R.id.addAirlineFragment))
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(databinding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun searchByNameOrId(searchTerm: String) {
        val bundle = Bundle()
        val id = searchTerm.toIntOrNull()
        if (id != null) {
            bundle.putString(SearchFragment.ID_PARAM, "$id")
        } else {
            bundle.putString(SearchFragment.TERM_PARAM, searchTerm)
        }
        mainViewModel.setNewDestination(FragmentDestination(R.id.searchingFragment, bundle))
    }

    private fun hideKeyboard() {
        val inputManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputManager?.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun onItemClick(view: View, airline: Airline) {
        val bundle = Bundle()
        bundle.putParcelable(AirlineDetailsFragment.AIRLINE_EXTRA, airline)
        mainViewModel.setNewDestination(FragmentDestination(R.id.airlineDetailsFragment, bundle))
    }

}

interface OnItemClick {
    fun onItemClick(view: View, airline: Airline)
}