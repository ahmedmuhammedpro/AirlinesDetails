package com.ahmed.airlinesdetails.model.repository

import com.ahmed.airlinesdetails.model.entities.Airline
import com.ahmed.airlinesdetails.model.entities.AirlineResponse
import com.ahmed.airlinesdetails.model.entities.AirlinesListResponse
import com.ahmed.airlinesdetails.model.entities.BaseResponse

interface AirlinesRepo {
    suspend fun getAirlines(): AirlinesListResponse
    suspend fun getAirLineById(id: String): AirlineResponse
    suspend fun addAirline(airline: Airline): BaseResponse
}