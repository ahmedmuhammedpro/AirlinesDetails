package com.ahmed.airlinesmodel.remote

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

class ReceiverInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())
        val statusCode = response.code()

        val jsonResponseObject = JSONObject()
        // Adding status code to all response.
        jsonResponseObject.put("statusCode", statusCode)
        jsonResponseObject.put("message", response.message())

        val jsonResponseBody: Any? = try { JSONTokener(response.body()?.string()).nextValue() } catch (ex: JSONException) { null }
        if (jsonResponseBody is JSONObject) {
            jsonResponseObject.put("value", jsonResponseBody)
        } else if (jsonResponseBody is JSONArray) {
            jsonResponseObject.put("items", jsonResponseBody)
        }

        return response.newBuilder().body(
            ResponseBody.create(
                response.body()?.contentType(),
                jsonResponseObject.toString()
            )
        ).build()
    }
}