package com.ahmed.airlinesdetails.model.entities

open class BaseResponse(var statusCode: Int? =null, var message: String = "") {

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

enum class ResponseState {
    SUCCESS,
    REDIRECT,
    CLIENT_ERROR,
    SERVER_ERROR,
    UNKNOWN_ERROR
}