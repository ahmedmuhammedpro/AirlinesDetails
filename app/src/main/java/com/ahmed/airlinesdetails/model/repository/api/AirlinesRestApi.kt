package com.ahmed.airlinesdetails.model.repository.api

import com.ahmed.airlinesdetails.model.entities.AirlinesResponse
import retrofit2.http.GET

interface AirlinesRestApi {

    @GET("airlines")
    suspend fun getAirlines(): AirlinesResponse
}