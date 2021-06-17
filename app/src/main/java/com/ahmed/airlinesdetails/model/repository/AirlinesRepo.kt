package com.ahmed.airlinesdetails.model.repository

import com.ahmed.airlinesdetails.model.entities.AirlinesResponse

interface AirlinesRepo {
    suspend fun getAirlines(): AirlinesResponse
}