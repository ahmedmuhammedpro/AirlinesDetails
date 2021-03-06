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
import com.ahmed.airlinesdetails.main_view.MainViewModel
import com.ahmed.airlinesdetails.utils.toIntOrFalse
import com.ahmed.airlinesmodel.AddingAirlineRepo
import com.ahmed.airlinesmodel.AirlinesRepositoryImp
import com.ahmed.airlinesmodel.entities.Airline
import com.ahmed.airlinesmodel.local.AirlineDatabase
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class AddAirlineFragment : BottomSheetDialogFragment() {

    private lateinit var addAirlineViewModel: AddAirlineViewModel

    private val addingAirlineRepo: AddingAirlineRepo by lazy {
        AirlinesRepositoryImp(AirlineDatabase.getDatabase(requireContext().applicationContext).airlineDao)
    }

    private lateinit var dataBinding: FragmentAddAirlineBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val designView =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            BottomSheetBehavior.from(designView).state = BottomSheetBehavior.STATE_EXPANDED
        }

        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_airline,
            container,
            false
        )

        addAirlineViewModel = ViewModelProvider(
            requireActivity(),
            AddAirlineViewModelFactory(addingAirlineRepo)
        ).get(AddAirlineViewModel::class.java)
        listenForAddAirline()

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


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
            if (it) {
                dataBinding.nameEditText.setText("")
                dataBinding.sloganEditText.setText("")
                dataBinding.countryEditText.setText("")
                dataBinding.headquarterEditText.setText("")
                dataBinding.establishedEditText.setText("")
                mainViewModel.refreshAirlines()
                dismiss()
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
        val headquarter = dataBinding.headquarterEditText.text.toString()
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
            return Airline(
                name,
                country,
                "",
                slogan,
                headquarter,
                "www.vodafone.com.eg",
                established
            )
        }

    }

    private fun showSnackBar(message: String) {
        Snackbar.make(requireActivity().findViewById(R.id.activity_main_container), message, Snackbar.LENGTH_LONG).show()
    }
}