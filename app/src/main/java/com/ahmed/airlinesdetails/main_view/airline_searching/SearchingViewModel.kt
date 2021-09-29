package com.ahmed.airlinesdetails.main_view.airline_searching

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.ahmed.airlinesmodel.SearchingAirlineRepo
import com.ahmed.airlinesmodel.entities.Airline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SearchingViewModel(private val searchingAirlineRepo: SearchingAirlineRepo) : ViewModel() {

    private val mAirlineLiveDate = MutableLiveData<Airline?>()
    val airlineLiveDate: LiveData<Airline?> = mAirlineLiveDate

    private val mLoadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = mLoadingLiveData

    private val mFailingLiveData = MutableLiveData<Boolean>()
    val failingLiveData: LiveData<Boolean> = mFailingLiveData

    fun getAirLineByName(name: String) {
        mLoadingLiveData.value = true
        viewModelScope.launch(Dispatchers.IO) {
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
    }

    fun getAirlineById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
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
    }

}

@Suppress("UNCHECKED_CAST")
class SearchingViewModelFactory(private val searchingAirlineRepo: SearchingAirlineRepo) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (SearchingViewModel(searchingAirlineRepo) as T)

}