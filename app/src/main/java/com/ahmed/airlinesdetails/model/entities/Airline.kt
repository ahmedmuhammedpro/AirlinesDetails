package com.ahmed.airlinesdetails.model.entities

import com.google.gson.annotations.SerializedName

data class AirlinesListResponse(@SerializedName("items") val items: List<Airline>) : BaseResponse()

data class AirlineResponse(@SerializedName("value") val airline: Airline) : BaseResponse()

data class Airline(
        val id: String?, val name: String, val country: String, val logo: String,
        val slogan: String, @SerializedName("head_quaters") val headQuarters: String,
        val website: String, val established: String
        )
