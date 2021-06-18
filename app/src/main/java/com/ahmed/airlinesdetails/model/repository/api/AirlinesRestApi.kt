package com.ahmed.airlinesdetails.model.repository.api

import com.ahmed.airlinesdetails.model.entities.Airline
import com.ahmed.airlinesdetails.model.entities.AirlineResponse
import com.ahmed.airlinesdetails.model.entities.AirlinesListResponse
import com.ahmed.airlinesdetails.model.entities.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AirlinesRestApi {

    @GET("airlines")
    suspend fun getAirlines(): AirlinesListResponse

    @GET("airlines/{id}")
    suspend fun getAirLineById(@Path("id") id: String): AirlineResponse

    @POST("airlines")
    suspend fun addAirline(@Body airline: Airline): BaseResponse
}