package com.ahmed.airlinesdetails.main_view.add_airline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed.airlinesdetails.model.entities.Airline
import com.ahmed.airlinesdetails.model.entities.BaseResponse
import com.ahmed.airlinesdetails.model.repository.AirlinesRepo
import com.ahmed.airlinesdetails.model.repository.AirlinesRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AddAirlineViewModel : ViewModel() {

    private val airlinesRepo: AirlinesRepo by lazy {
        AirlinesRepoImpl()
    }

    private val mAddAirlineLiveData = MutableLiveData<BaseResponse>()
    val addAirlineLiveData: LiveData<BaseResponse> = mAddAirlineLiveData

    private val mLoadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = mLoadingLiveData

    private val mFailingLiveData = MutableLiveData<Boolean>()
    val failingLiveData: LiveData<Boolean> = mFailingLiveData

    fun addAirline(airline: Airline) {
        mLoadingLiveData.value = true
        viewModelScope.launch(Dispatchers.IO) {
            delay(5000)
            try {
                val response = airlinesRepo.addAirline(airline)
                withContext(Dispatchers.Main) {
                    mAddAirlineLiveData.value = response
                }
            } catch (ex: Exception) {
                Timber.e(ex)
                mFailingLiveData.value = true
            }

            withContext(Dispatchers.Main) {
                mLoadingLiveData.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        val x = 10
    }
}