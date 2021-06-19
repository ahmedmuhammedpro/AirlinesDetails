package com.ahmed.airlinesdetails.main_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainViewModel : ViewModel() {

    private val mNavDestination = MutableLiveData<FragmentDestination>()
    val navDestination: LiveData<FragmentDestination> = mNavDestination

    fun setNewDestination(destination: FragmentDestination) {
        mNavDestination.value = destination
    }
}