package com.ahmed.airlinesdetails.model.repository

import com.ahmed.airlinesdetails.model.entities.Airline
import com.ahmed.airlinesdetails.model.entities.AirlineResponse
import com.ahmed.airlinesdetails.model.entities.AirlinesListResponse
import com.ahmed.airlinesdetails.model.entities.BaseResponse
import com.ahmed.airlinesdetails.model.repository.api.AirlinesApi

class AirlinesRepoImpl(val version: Int = 1) : AirlinesRepo {

    private var airlinesApi = AirlinesApi.getAirlineRestApi(version)

    override suspend fun getAirlines(): AirlinesListResponse {
        return airlinesApi.getAirlines()
    }

    override suspend fun getAirLineById(id: String): AirlineResponse {
        return airlinesApi.getAirLineById(id)
    }

    override suspend fun addAirline(airline: Airline): BaseResponse {
        return airlinesApi.addAirline(airline)
    }

}