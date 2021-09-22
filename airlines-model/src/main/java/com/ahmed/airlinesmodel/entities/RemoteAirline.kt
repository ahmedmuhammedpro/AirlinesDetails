package com.ahmed.airlinesmodel.entities

import com.google.gson.annotations.SerializedName

enum class ResponseState {
    SUCCESS,
    REDIRECT,
    CLIENT_ERROR,
    SERVER_ERROR,
    UNKNOWN_ERROR
}

open class BaseResponse(var statusCode: Int? = null, var message: String = "") {

    fun getResponseState(): ResponseState {
        return when (statusCode) {
            in 200 until 300 -> ResponseState.SUCCESS
            in 300 until 400 -> ResponseState.REDIRECT
            in 400 until 500 -> ResponseState.CLIENT_ERROR
            in 500 until 600 -> ResponseState.SERVER_ERROR
            else -> ResponseState.UNKNOWN_ERROR
        }
    }
}

data class RemoteAirlinesResponse(
    @SerializedName("items") val items: ArrayList<Airline>
) : BaseResponse(), Mapper<ArrayList<Airline>> {
    override fun map(): ArrayList<Airline> {
        return items
    }
}

data class RemoteAirlineResponse(
    @SerializedName("value") val airline: Airline?
) : BaseResponse(), Mapper<Airline> {
    override fun map(): Airline? {
        return airline
    }
}