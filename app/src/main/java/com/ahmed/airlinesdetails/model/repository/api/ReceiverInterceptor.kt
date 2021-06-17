package com.ahmed.airlinesdetails.model.repository.api

import org.json.JSONObject
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONTokener

class ReceiverInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())
        val statusCode = response.code()

        val jsonObject = JSONObject()
        // Adding status code to all response.
        jsonObject.put("statusCode", statusCode)

        if (statusCode !in 200 until 300) {
            jsonObject.put("message", response.message())
        } else {
            val jsonBody = JSONTokener(response.body()?.string()).nextValue()
            if (jsonBody is JSONObject) {
                jsonBody.keys().forEach {
                    jsonObject.put(it, jsonBody.get(it))
                }
            } else if (jsonBody is JSONArray) {
                jsonObject.put("items", jsonBody)
            }
        }

        return response.newBuilder().body(ResponseBody.create(response.body()?.contentType(), jsonObject.toString())).build()
    }
}