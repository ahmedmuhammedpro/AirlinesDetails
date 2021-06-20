package com.ahmed.airlinesdetails.model.repository

import com.ahmed.airlinesdetails.model.entities.Airline
import com.ahmed.airlinesdetails.model.entities.AirlineResponse
import com.ahmed.airlinesdetails.model.entities.AirlinesListResponse
import com.ahmed.airlinesdetails.model.entities.BaseResponse
import com.ahmed.airlinesdetails.model.repository.api.AirlinesApi
import com.ahmed.airlinesdetails.model.repository.api.AirlinesRestApi

class AirlinesRepoImpl(val airlinesApi: AirlinesRestApi) : AirlinesRepo {

    override suspend fun getAirlines(): AirlinesListResponse {
        return airlinesApi.getAirlines()
    }

    override suspend fun getAirLineById(id: String): AirlineResponse {
        return airlinesApi.getAirLineById(id)
    }

    override suspend fun addAirline(airline: Airline): BaseResponse {
        return airlinesApi.addAirline(airline)
    }

    companion object {

        @Volatile
        private var INSTANCE: AirlinesRepo? = null

        fun getInstance(version: Int = 1): AirlinesRepo {
            return INSTANCE ?: synchronized(this) {
                val airlinesApi = AirlinesApi.getAirlineRestApi(version)
                AirlinesRepoImpl(airlinesApi)
            }
        }
    }

}