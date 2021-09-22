package com.ahmed.airlinesdetails.main_view.airlines_listing

import androidx.lifecycle.*
import com.ahmed.airlinesmodel.AirlinesRepository
import com.ahmed.airlinesmodel.entities.Airline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AirlineListingViewModel(private val airlinesRepo: AirlinesRepository) : ViewModel() {

    private val jobList = ArrayList<Job>()

    private val mAirlinesLiveDate = MutableLiveData<ArrayList<Airline>?>()
    val airlinesLiveDate: LiveData<ArrayList<Airline>?> = mAirlinesLiveDate

    private val mLoadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = mLoadingLiveData

    private val mFailingLiveData = MutableLiveData<Boolean>()
    val failingLiveData: LiveData<Boolean> = mFailingLiveData

    fun getAirlines() {
        mLoadingLiveData.value = true
        val job = viewModelScope.launch(Dispatchers.IO) {
            var airlines: ArrayList<Airline>? = null

            try {
                airlines = airlinesRepo.getAllLocalAirlines()
                val result = airlinesRepo.getAllRemoteAirlines()
                airlines = result.getOrThrow()
                airlinesRepo.deleteAllLocalAirlines()
                airlines?.let { airlinesRepo.insertAllAirlines(it) }
            } catch (ex: Throwable) {
                Timber.e(ex)
                withContext(Dispatchers.Main) {
                    mFailingLiveData.value = false
                }
            }

            withContext(Dispatchers.Main) {
                mAirlinesLiveDate.value = airlines
                mLoadingLiveData.value = false
            }
        }

        jobList.add(job)
    }

    override fun onCleared() {
        super.onCleared()
        jobList.forEach {
            if (it.isActive) {
                it.cancel()
            }
        }

        jobList.clear()
    }
}

@Suppress("UNCHECKED_CAST")
class AirlineListingViewModelFactory(private val airlinesRepo: AirlinesRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (AirlineListingViewModel(airlinesRepo) as T)

}