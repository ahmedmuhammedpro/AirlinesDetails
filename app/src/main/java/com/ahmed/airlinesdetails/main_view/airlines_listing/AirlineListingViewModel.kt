package com.ahmed.airlinesdetails.main_view.airlines_listing

import androidx.lifecycle.*
import com.ahmed.airlinesdetails.model.entities.Airline
import com.ahmed.airlinesdetails.model.entities.AirlinesListResponse
import com.ahmed.airlinesdetails.model.repository.AirlinesRepo
import com.ahmed.airlinesdetails.model.repository.api.FailingTypes
import com.google.gson.JsonParseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException

class AirlineListingViewModel(private val airlinesRepo: AirlinesRepo) : ViewModel() {

    private val jobList = ArrayList<Job>()

    private val mAirlinesLiveDate = MutableLiveData<AirlinesListResponse>()
    val airlinesLiveDate: LiveData<AirlinesListResponse> = mAirlinesLiveDate

    private val mLoadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = mLoadingLiveData

    private val mFailingLiveData = MutableLiveData<FailingTypes>()
    val failingLiveData: LiveData<FailingTypes> = mFailingLiveData

    fun getAirlines() {
        mLoadingLiveData.value = true
        val job = viewModelScope.launch(Dispatchers.IO) {

            val exception: Exception? = try {
                val airlines = airlinesRepo.getAirlines()
                withContext(Dispatchers.Main) { mAirlinesLiveDate.value = airlines }
                null
            } catch (ex: Exception) {
                ex
            }

            withContext(Dispatchers.Main) {

                exception?.let {
                    Timber.e(it)
                    when (it) {
                        is SocketTimeoutException -> mFailingLiveData.value = FailingTypes.TIMEOUT_FAILING
                        is JsonParseException -> mFailingLiveData.value = FailingTypes.JSON_PARSE_FAILING
                        is IOException -> mFailingLiveData.value = FailingTypes.NETWORK_ERROR
                        else -> mFailingLiveData.value = FailingTypes.UNKNOWN_FAILING
                    }
                }

                mLoadingLiveData.value = false
            }
        }

        jobList.add(job)
    }

    fun getAirLineByName(name: String) {
        mLoadingLiveData.value = true
        val job = viewModelScope.launch(Dispatchers.IO) {

            val exception: Exception? = try {
                val airlines = airlinesRepo.getAirlines()
                val airline: Airline? = airlines.items.find { it.name == name }
                withContext(Dispatchers.Main) {
                    if (airline != null) {
                        val airlinesResponse = AirlinesListResponse(arrayListOf(airline))
                        airlinesResponse.statusCode = airlines.statusCode
                        airlinesResponse.message = airlines.message
                        mAirlinesLiveDate.value = airlinesResponse
                    } else {
                        val airlinesResponse = AirlinesListResponse(arrayListOf())
                        airlinesResponse.statusCode = airlines.statusCode
                        airlinesResponse.message = airlines.message
                        mAirlinesLiveDate.value = airlinesResponse
                    }
                }

                null
            } catch (ex: Exception) {
                ex
            }

            withContext(Dispatchers.Main) {

                exception?.let {
                    Timber.e(it)
                    when (it) {
                        is SocketTimeoutException -> mFailingLiveData.value = FailingTypes.TIMEOUT_FAILING
                        is JsonParseException -> mFailingLiveData.value = FailingTypes.JSON_PARSE_FAILING
                        else -> mFailingLiveData.value = FailingTypes.UNKNOWN_FAILING
                    }
                }

                mLoadingLiveData.value = false
            }
        }

        jobList.add(job)
    }

    fun getAirlineById(id: String) {
        val job = viewModelScope.launch(Dispatchers.IO) {

            val exception: Exception? = try {
                val airlineResponse = airlinesRepo.getAirLineById(id)
                withContext(Dispatchers.Main) {
                    if (airlineResponse.airline != null) {
                        val airlinesResponse = AirlinesListResponse(arrayListOf(airlineResponse.airline))
                        airlinesResponse.statusCode = airlineResponse.statusCode
                        airlinesResponse.message = airlineResponse.message
                        mAirlinesLiveDate.value = airlinesResponse
                    } else {
                        val airlinesResponse = AirlinesListResponse(arrayListOf())
                        airlinesResponse.statusCode = airlineResponse.statusCode
                        airlinesResponse.message = airlineResponse.message
                        mAirlinesLiveDate.value = airlinesResponse
                    }
                }
                null
            } catch (ex: Exception) {
                ex
            }

            withContext(Dispatchers.Main) {

                exception?.let {
                    Timber.e(it)
                    when (it) {
                        is SocketTimeoutException -> mFailingLiveData.value = FailingTypes.TIMEOUT_FAILING
                        is JsonParseException -> mFailingLiveData.value = FailingTypes.JSON_PARSE_FAILING
                        else -> mFailingLiveData.value = FailingTypes.UNKNOWN_FAILING
                    }
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
class AirlineListingViewModelFactory(private val airlinesRepo: AirlinesRepo) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (AirlineListingViewModel(airlinesRepo) as T)

}