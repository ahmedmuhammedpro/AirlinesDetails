package com.ahmed.airlinesdetails.main_view.add_airline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.ahmed.airlinesmodel.AddingAirlineRepo
import com.ahmed.airlinesmodel.entities.Airline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AddAirlineViewModel(private val addingAirlineRepo: AddingAirlineRepo) : ViewModel() {

    private val mAddAirlineLiveData = MutableLiveData<Boolean>()
    val addAirlineLiveData: LiveData<Boolean> = mAddAirlineLiveData

    private val mLoadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = mLoadingLiveData

    private val mFailingLiveData = MutableLiveData<Boolean>()
    val failingLiveData: LiveData<Boolean> = mFailingLiveData

    fun addAirline(airline: Airline) {
        mLoadingLiveData.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = addingAirlineRepo.addAirline(airline)
            withContext(Dispatchers.Main) {
                try {
                    result.getOrThrow()
                    mAddAirlineLiveData.value = true
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
class AddAirlineViewModelFactory(private val addingAirlineRepo: AddingAirlineRepo) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (AddAirlineViewModel(addingAirlineRepo) as T)

}