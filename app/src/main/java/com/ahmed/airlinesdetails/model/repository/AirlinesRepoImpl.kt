package com.ahmed.airlinesdetails.model.repository

import com.ahmed.airlinesdetails.model.entities.AirlinesResponse
import com.ahmed.airlinesdetails.model.repository.api.AirlinesApi

class AirlinesRepoImpl(val version: Int = 1) : AirlinesRepo {

    private var airlinesApi = AirlinesApi.getAirlineRestApi(version)

    override suspend fun getAirlines(): AirlinesResponse {
        return airlinesApi.getAirlines()
    }

}