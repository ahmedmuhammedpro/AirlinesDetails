package com.ahmed.airlinesmodel.remote

import com.ahmed.airlinesmodel.entities.RemoteAirlinesResponse
import com.ahmed.airlinesmodel.entities.RemoteAirlineResponse
import com.ahmed.airlinesmodel.entities.BaseResponse
import com.ahmed.airlinesmodel.entities.Airline
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AirlinesRestApi {

    @GET("airlines")
    suspend fun getAirlines(): RemoteAirlinesResponse

    @GET("airlines/{id}")
    suspend fun getAirLineById(@Path("id") id: String): RemoteAirlineResponse

    @POST("airlines")
    suspend fun addAirline(@Body airline: Airline): BaseResponse
}