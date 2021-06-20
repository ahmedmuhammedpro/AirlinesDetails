package com.ahmed.airlinesdetails.model.repository

import com.ahmed.airlinesdetails.model.entities.Airline
import com.ahmed.airlinesdetails.model.entities.AirlineResponse
import com.ahmed.airlinesdetails.model.entities.AirlinesListResponse
import com.ahmed.airlinesdetails.model.entities.BaseResponse
import com.ahmed.airlinesdetails.model.repository.api.AirlinesRestApi

class FakeAirlineRepo(val airlinesRestApi: AirlinesRestApi) : AirlinesRepo {

    override suspend fun getAirlines(): AirlinesListResponse {
        return airlinesRestApi.getAirlines()
    }

    override suspend fun getAirLineById(id: String): AirlineResponse {
        return airlinesRestApi.getAirLineById(id)
    }

    override suspend fun addAirline(airline: Airline): BaseResponse {
        return airlinesRestApi.addAirline(airline)
    }
}