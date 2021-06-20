package com.ahmed.airlinesdetails.model.repository

import com.ahmed.airlinesdetails.model.entities.Airline
import com.ahmed.airlinesdetails.model.entities.AirlineResponse
import com.ahmed.airlinesdetails.model.entities.AirlinesListResponse
import com.ahmed.airlinesdetails.model.entities.BaseResponse
import com.ahmed.airlinesdetails.model.repository.api.AirlinesRestApi

class FakeAirlinesApi(val airlines: ArrayList<Airline> = arrayListOf(), val statusCode: Int = 200) : AirlinesRestApi {

    override suspend fun getAirlines(): AirlinesListResponse {
        val airlinesListResponse = AirlinesListResponse(airlines)
        airlinesListResponse.statusCode = statusCode
        return airlinesListResponse
    }

    override suspend fun getAirLineById(id: String): AirlineResponse {
        val airline = airlines.find { it.id == id}
        val airlineResponse = AirlineResponse(airline)
        airlineResponse.statusCode = statusCode
        return airlineResponse
    }

    override suspend fun addAirline(airline: Airline): BaseResponse {
        if (statusCode in 20 until 300) {
            airlines.add(airline)
        }

        return BaseResponse(statusCode)
    }
}