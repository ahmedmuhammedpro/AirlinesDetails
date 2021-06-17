package com.ahmed.airlinesdetails.model.entities

import com.google.gson.annotations.SerializedName

data class AirlinesResponse(val items: List<Airline>) : BaseResponse()

data class Airline(
        val id: String, val name: String, val country: String,
        val slogan: String, @SerializedName("head_quaters") val headQuarters: String,
        val website: String, val established: String
        )
