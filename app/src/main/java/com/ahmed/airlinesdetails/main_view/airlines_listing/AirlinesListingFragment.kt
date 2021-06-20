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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmed.airlinesdetails.R
import com.ahmed.airlinesdetails.databinding.FragmentAirlinesListingBinding
import com.ahmed.airlinesdetails.main_view.FragmentDestination
import com.ahmed.airlinesdetails.main_view.MainViewModel
import com.ahmed.airlinesdetails.main_view.details.AirlineDetailsFragment
import com.ahmed.airlinesdetails.model.entities.Airline
import com.ahmed.airlinesdetails.model.entities.ResponseState
import com.ahmed.airlinesdetails.model.repository.AirlinesRepoImpl
import com.ahmed.airlinesdetails.model.repository.api.FailingTypes
import com.ahmed.airlinesdetails.utils.copyRange
import com.ahmed.airlinesdetails.utils.toIntOrFalse

class AirlinesListFragment : Fragment(), OnItemClick {

    private lateinit var airlinesViewModel: AirlineListingViewModel
    private lateinit var databinding: FragmentAirlinesListingBinding
    private lateinit var airlinesAdapter: AirlinesAdapter
    private lateinit var mainViewModel: MainViewModel
    private var mAirlines = ArrayList<Airline>()

    // Custom Paging
    private val recyclerViewScrollListener = object : RecyclerView.OnScrollListener() {

        private var currentAppendedItems = 20

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            if (currentAppendedItems - 1 <= layoutManager.findLastVisibleItemPosition()) {
                val nextTwenty = mAirlines.copyRange(currentAppendedItems, currentAppendedItems + 20)
                currentAppendedItems += 20
                airlinesAdapter.items.addAll(nextTwenty)
                airlinesAdapter.notifyDataSetChanged()
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        databinding = DataBindingUtil.inflate(inflater, R.layout.fragment_airlines_listing, container, false)
        airlinesViewModel =
            ViewModelProvider(requireActivity(), AirlineListingViewModelFactory(AirlinesRepoImpl.getInstance()))
                .get(AirlineListingViewModel::class.java)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        databinding.viewModel = airlinesViewModel
        databinding.lifecycleOwner = this
        airlinesAdapter = AirlinesAdapter(arrayListOf(), this)
        databinding.airlinesRecyclerView.adapter = airlinesAdapter
        databinding.airlinesRecyclerView.addOnScrollListener(recyclerViewScrollListener)

        airlinesViewModel.getAirlines()
        listenForAirlinesResponse()

        setUpViewListener()

        return databinding.root
    }

    private fun listenForAirlinesResponse() {
        airlinesViewModel.airlinesLiveDate.observe(viewLifecycleOwner) {
            when (it.getResponseState()) {
                ResponseState.SUCCESS -> {
                    if (!it.items.isNullOrEmpty()) {
                        databinding.mainContainer.visibility = View.VISIBLE
                        mAirlines = it.items
                        val firstTwenty = mAirlines.copyRange(exclusiveEnd = 20)
                        airlinesAdapter.items.clear()
                        airlinesAdapter.items.addAll(firstTwenty)
                        airlinesAdapter.notifyDataSetChanged()
                    } else {
                        databinding.mainContainer.visibility = View.GONE
                        databinding.noDataContainer.visibility = View.VISIBLE
                    }
                }

                ResponseState.CLIENT_ERROR -> showFailingErrorView(
                    true,
                    getString(R.string.general_error)
                )

                ResponseState.SERVER_ERROR, ResponseState.UNKNOWN_ERROR, ResponseState.REDIRECT ->
                    showFailingErrorView(true, getString(R.string.general_error))
            }
        }

        airlinesViewModel.loadingLiveData.observe(viewLifecycleOwner) {
            databinding.loadingViewContainer.visibility = if (it) View.VISIBLE else View.GONE
        }

        airlinesViewModel.failingLiveData.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {

                    FailingTypes.TIMEOUT_FAILING -> showFailingErrorView(
                        true,
                        getString(R.string.slow_connection_error)
                    )

                    FailingTypes.NETWORK_ERROR -> showFailingErrorView(
                        true,
                        getString(R.string.network_error)
                    )

                    FailingTypes.JSON_PARSE_FAILING -> showFailingErrorView(
                        false,
                        getString(R.string.general_error)
                    )

                    FailingTypes.UNKNOWN_FAILING -> showFailingErrorView(
                        false,
                        getString(R.string.general_error)
                    )

                }
            }
        }
    }

    private fun setUpViewListener() {

        databinding.tryAgain.setOnClickListener {
            databinding.errorViewContainer.visibility = View.GONE
            databinding.loadingViewContainer.visibility = View.VISIBLE
            airlinesViewModel.getAirlines()
        }

        databinding.searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId  == EditorInfo.IME_ACTION_SEARCH) {
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
                databinding.mainContainer.visibility = View.GONE
                searchByNameOrId(searchTerm)
                hideKeyboard()
            }
        }

        databinding.goBack.setOnClickListener {
            databinding.noDataContainer.visibility = View.GONE
            airlinesViewModel.getAirlines()
        }

        databinding.addAirlineButton.setOnClickListener {
            mainViewModel.setNewDestination(FragmentDestination(R.id.addAirlineFragment))
        }
    }

    private fun showFailingErrorView(isNetworkError: Boolean, message: String) {
        databinding.errorViewContainer.visibility = View.VISIBLE
        databinding.mainContainer.visibility = View.GONE
        databinding.loadingViewContainer.visibility = View.GONE
        if (!isNetworkError) {
            databinding.errorImageView.setImageResource(R.drawable.ic_general_error)
        }

        databinding.errorTextView.text = message
    }

    private fun searchByNameOrId(searchTerm: String?) {
        if (!searchTerm.isNullOrEmpty()) {
            if (searchTerm.toIntOrFalse()) {
                airlinesViewModel.getAirlineById(searchTerm)
            } else {
                airlinesViewModel.getAirLineByName(searchTerm)
            }
        }
    }

    private fun hideKeyboard() {
        val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputManager?.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun onItemClick(view: View, airline: Airline) {
        val bundle = Bundle()
        bundle.putParcelable(AirlineDetailsFragment.AIRLINE_EXTRA, airline)
        mainViewModel.setNewDestination(FragmentDestination(R.id.airlineDetailsFragment, bundle))
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AirlinesListFragment()
    }

}

interface OnItemClick {
    fun onItemClick(view: View, airline: Airline)
}