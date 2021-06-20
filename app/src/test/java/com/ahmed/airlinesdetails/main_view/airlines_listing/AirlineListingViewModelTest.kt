package com.ahmed.airlinesdetails.main_view.airlines_listing

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ahmed.airlinesdetails.MainCoroutineRule
import com.ahmed.airlinesdetails.getOrAwaitValue
import com.ahmed.airlinesdetails.model.entities.Airline
import com.ahmed.airlinesdetails.model.entities.ResponseState
import com.ahmed.airlinesdetails.model.repository.FakeAirlineRepo
import com.ahmed.airlinesdetails.model.repository.FakeAirlinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class AirlineListingViewModelTest {

    private val airline1 = Airline("10", "A Airline", "Egypt", "", "", "", "www.google.com", "1980")
    private val airline2 = Airline("11", "B Airline", "Egypt", "", "", "", "www.google.com", "1980")
    private val airline3 = Airline("12", "C Airline", "Egypt", "", "", "", "www.google.com", "1980")
    private val airline4 = Airline("13", "D Airline", "Egypt", "", "", "", "www.google.com", "1980")

    private lateinit var airlineListingViewModel: AirlineListingViewModel

    private lateinit var airlinesRepo: FakeAirlineRepo

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        airlinesRepo = FakeAirlineRepo(FakeAirlinesApi(arrayListOf(airline1, airline2, airline3, airline4)))
        airlineListingViewModel = AirlineListingViewModel(airlinesRepo)

    }

    @Test
    fun getAirlines() {
        airlineListingViewModel.getAirlines()
        val value = airlineListingViewModel.airlinesLiveDate.getOrAwaitValue()
        assertEquals(value.items.size, 4)
        assertEquals(value.getResponseState(), ResponseState.SUCCESS)
    }


    @Test
    fun getAirlineByName() {
        airlineListingViewModel.getAirLineByName("A Airline")
        val value = airlineListingViewModel.airlinesLiveDate.getOrAwaitValue()
        assertEquals(value.getResponseState(), ResponseState.SUCCESS)
        assertEquals(value.items.size, 1)
        assertEquals(value.items[0], airline1)
    }

}