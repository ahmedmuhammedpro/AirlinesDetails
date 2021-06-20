package com.ahmed.airlinesdetails.model.repository

import com.ahmed.airlinesdetails.model.entities.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@ExperimentalCoroutinesApi
class AirlinesRepoImplTest {

    private val airline1 = Airline("10", "A Airline", "Egypt", "", "", "", "www.google.com", "1980")
    private val airline2 = Airline("11", "A Airline", "Egypt", "", "", "", "www.google.com", "1980")
    private val airline3 = Airline("12", "A Airline", "Egypt", "", "", "", "www.google.com", "1980")
    private val airline4 = Airline("13", "A Airline", "Egypt", "", "", "", "www.google.com", "1980")

    private val airlinesResponse = AirlinesListResponse(arrayListOf(airline1, airline2, airline3, airline4))
    private val airlineResponse = AirlineResponse(airline1)
    private val baseResponse = BaseResponse(200)

    private lateinit var airlinesApi: FakeAirlinesApi
    private lateinit var airlineRepo: AirlinesRepo

    @Before
    fun createRepo() {
        airlinesResponse.statusCode = 200
        airlineResponse.statusCode = 200

        airlinesApi = FakeAirlinesApi(arrayListOf(airline1, airline2, airline3, airline4))
        airlineRepo = AirlinesRepoImpl(airlinesApi)
    }

    @Test
    fun getAirlines_fromAirlinesApi() = runBlockingTest {
        val airlinesResponseResult = airlinesApi.getAirlines()
        assertEquals(airlinesResponse, airlinesResponseResult)
        assertEquals(airlinesResponse.getResponseState(), ResponseState.SUCCESS)
    }

    @Test
    fun getAirline_fromAirlinesApi() = runBlockingTest {
        val airlineResponseResult = airlinesApi.getAirLineById("10")
        assertEquals(airlineResponse, airlineResponseResult)
        assertEquals(airlinesResponse.getResponseState(), ResponseState.SUCCESS)
    }


    @Test
    fun addAirline_fromAirlinesApi() = runBlockingTest {
        val sizeBefore = airlinesApi.airlines.size
        val addAirlineResult = airlinesApi.addAirline(airline1)
        assertEquals(addAirlineResult.statusCode, baseResponse.statusCode)
        assertEquals(addAirlineResult.getResponseState(), ResponseState.SUCCESS)
        assertEquals(sizeBefore + 1, airlinesApi.airlines.size)
    }

}