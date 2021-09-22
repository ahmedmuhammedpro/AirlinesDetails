package com.ahmed.airlinesdetails.main_view.airline_searching

import androidx.lifecycle.*
import com.ahmed.airlinesmodel.SearchingAirlineRepo
import com.ahmed.airlinesmodel.entities.Airline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SearchingViewModel(private val searchingAirlineRepo: SearchingAirlineRepo) : ViewModel() {

    private val jobList = ArrayList<Job>()

    private val mAirlineLiveDate = MutableLiveData<Airline?>()
    val airlineLiveDate: LiveData<Airline?> = mAirlineLiveDate

    private val mLoadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = mLoadingLiveData

    private val mFailingLiveData = MutableLiveData<Boolean>()
    val failingLiveData: LiveData<Boolean> = mFailingLiveData

    fun getAirLineByName(name: String) {
        mLoadingLiveData.value = true
        val job = viewModelScope.launch(Dispatchers.IO) {
            val result = searchingAirlineRepo.getAirlineByName(name)
            withContext(Dispatchers.Main) {
                try {
                    mAirlineLiveDate.value = result.getOrThrow()
                } catch (ex: Throwable) {
                    Timber.e(ex)
                    mFailingLiveData.value = true
                }
                mLoadingLiveData.value = false
            }
        }

        jobList.add(job)
    }

    fun getAirlineById(id: String) {
        val job = viewModelScope.launch(Dispatchers.IO) {
            val result = searchingAirlineRepo.getAirLineById(id)
            withContext(Dispatchers.Main) {
                try {
                    mAirlineLiveDate.value = result.getOrThrow()
                } catch (ex: Throwable) {
                    Timber.e(ex)
                    mFailingLiveData.value = true
                }
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
class SearchingViewModelFactory(private val searchingAirlineRepo: SearchingAirlineRepo) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (SearchingViewModel(searchingAirlineRepo) as T)

}