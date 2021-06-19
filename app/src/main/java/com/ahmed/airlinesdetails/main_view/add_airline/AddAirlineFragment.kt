package com.ahmed.airlinesdetails.main_view.add_airline

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ahmed.airlinesdetails.R
import com.ahmed.airlinesdetails.databinding.FragmentAddAirlineBinding
import com.ahmed.airlinesdetails.model.entities.Airline
import com.ahmed.airlinesdetails.model.entities.ResponseState
import com.ahmed.airlinesdetails.utils.toIntOrFalse
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class AddAirlineFragment : BottomSheetDialogFragment() {

    private lateinit var addAirlineViewModel: AddAirlineViewModel
    private lateinit var dataBinding: FragmentAddAirlineBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val designView =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            BottomSheetBehavior.from(designView).state = BottomSheetBehavior.STATE_EXPANDED
        }

        return bottomSheetDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_airline, container, false)
        addAirlineViewModel = ViewModelProvider(requireActivity()).get(AddAirlineViewModel::class.java)
        listenForAddAirline()

        dataBinding.confirmViewContainer.setOnClickListener {
            val airline = createAirline()
            airline?.let {
                addAirlineViewModel.addAirline(it)
            }
        }

        dataBinding.cancelButton.setOnClickListener {
            dismiss()
        }

        return dataBinding.root
    }

    private fun listenForAddAirline() {
        addAirlineViewModel.addAirlineLiveData.observe(viewLifecycleOwner) {
            when (it.getResponseState()) {
                ResponseState.SUCCESS -> {
                    showSnackBar("Airline is successfully added")
                    dataBinding.nameEditText.setText("")
                    dataBinding.sloganEditText.setText("")
                    dataBinding.countryEditText.setText("")
                    dataBinding.headquarterEditText.setText("")
                    dataBinding.establishedEditText.setText("")
                }
                ResponseState.CLIENT_ERROR, ResponseState.UNKNOWN_ERROR,
                ResponseState.SERVER_ERROR, ResponseState.REDIRECT -> showSnackBar(getString(R.string.general_error))
            }
        }

        addAirlineViewModel.loadingLiveData.observe(viewLifecycleOwner) {
            dataBinding.confirmViewContainer.isEnabled = !it
            dataBinding.loadingView.visibility = if (it) View.VISIBLE else View.GONE
            dataBinding.confirmButton.visibility = if (it) View.GONE else View.VISIBLE
        }

        addAirlineViewModel.failingLiveData.observe(viewLifecycleOwner) {
            showSnackBar(getString(R.string.general_error))
        }
    }

    private fun createAirline(): Airline? {
        val name = dataBinding.nameEditText.text.toString()
        val slogan = dataBinding.sloganEditText.text.toString()
        val country = dataBinding.countryEditText.text.toString()
        val headquarter = dataBinding.headquarterEditText.toString()
        val established = dataBinding.establishedEditText.text.toString()

        if (name.isEmpty()) {
            showSnackBar("Please Type Airline name")
            return null
        } else if (slogan.isEmpty()) {
            showSnackBar("Please Type Slogan")
            return null
        } else if (country.isEmpty()) {
            showSnackBar("Please Type Country")
            return null
        } else if (headquarter.isEmpty()) {
            showSnackBar("Please Type Headquarter")
            return null
        } else if (established.isEmpty()) {
            showSnackBar("Please Type Established year")
            return null
        } else if (!established.toIntOrFalse()) {
            showSnackBar("Please Type a Established year")
            return null
        } else {
            return Airline(null, name, country, "", slogan, headquarter, "", established)
        }

    }

    private fun showSnackBar(message: String) {
        Snackbar.make(dataBinding.root, message, Snackbar.LENGTH_LONG).show()
    }
}