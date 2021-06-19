package com.ahmed.airlinesdetails.main_view.error_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ahmed.airlinesdetails.R
import com.ahmed.airlinesdetails.databinding.FragmentErrorBinding

class ErrorFragment : Fragment() {

    private lateinit var dataBinding: FragmentErrorBinding
    private var isNetworkError = false
    private var isGeneralError = false
    private var errorMessage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isNetworkError = it.getBoolean(IS_NETWORK_ERROR_KEY)
            isGeneralError = it.getBoolean(IS_GENERAL_ERROR_KEY)
            errorMessage = it.getString(ERROR_MESSAGE) ?: ""
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_error, container, false)

        if (isGeneralError) {
            dataBinding.errorImageView.setImageResource(R.drawable.ic_general_error)
        }

        dataBinding.errorTextView.text = errorMessage

        return dataBinding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }


    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }

    companion object {

        const val IS_NETWORK_ERROR_KEY = "ErrorFragment.is_network_error"
        const val IS_GENERAL_ERROR_KEY = "ErrorFragment.is_general_error"
        const val ERROR_MESSAGE = "ErrorFragment.error_message"

        @JvmStatic
        fun newInstance(isNetworkError: Boolean = false, isGeneralError: Boolean = false, errorMessage: String) =
            ErrorFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_NETWORK_ERROR_KEY, isNetworkError)
                    putBoolean(IS_GENERAL_ERROR_KEY, isGeneralError)
                    putString(ERROR_MESSAGE, errorMessage)
                }
            }
    }
}