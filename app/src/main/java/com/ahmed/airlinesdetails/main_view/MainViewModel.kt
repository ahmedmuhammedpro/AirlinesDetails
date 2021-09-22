package com.ahmed.airlinesdetails.main_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainViewModel : ViewModel() {

    private val mNavDestination = MutableLiveData<FragmentDestination>()
    val navDestination: LiveData<FragmentDestination> = mNavDestination

    private val mShouldRefreshAirlines = MutableLiveData<Boolean>()
    val shouldRefreshAirlines: LiveData<Boolean> = mShouldRefreshAirlines

    fun setNewDestination(destination: FragmentDestination) {
        mNavDestination.value = destination
    }

    fun refreshAirlines() {
        mShouldRefreshAirlines.value = true
    }
}