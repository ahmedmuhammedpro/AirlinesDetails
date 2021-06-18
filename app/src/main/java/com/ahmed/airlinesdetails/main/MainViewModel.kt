package com.ahmed.airlinesdetails.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmed.airlinesdetails.model.entities.BaseResponse


class MainViewModel : ViewModel() {

    private val mNavDestination = MutableLiveData<FragmentDestination>()
    val navDestination: LiveData<FragmentDestination> = mNavDestination

    fun setNewDestination(destination: FragmentDestination) {
        mNavDestination.value = destination
    }
}