package com.ahmed.airlinesdetails.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed.airlinesdetails.model.repository.AirlinesRepoImpl
import com.ahmed.airlinesdetails.model.entities.AirlinesResponse
import com.ahmed.airlinesdetails.model.repository.AirlinesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val airlinesRepo: AirlinesRepo = AirlinesRepoImpl()
    private val mAirlinesLiveDate = MutableLiveData<AirlinesResponse>()
    val airlinesLiveDate: LiveData<AirlinesResponse> = mAirlinesLiveDate

    fun getAirlines() {
        viewModelScope.launch(Dispatchers.IO) {
            val airlines = airlinesRepo.getAirlines()
            withContext(Dispatchers.Main) {
                mAirlinesLiveDate.value = airlines
            }
        }
    }
}