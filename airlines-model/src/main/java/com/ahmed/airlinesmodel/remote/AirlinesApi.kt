package com.ahmed.airlinesmodel.remote

import androidx.multidex.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AirlinesApi {

    private const val BASE_URL = "https://api.instantwebtools.net/v"

    fun getAirlineRestApi(version: Int = 1): AirlinesRestApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("$BASE_URL$version/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOKHttp())
            .build()

        return retrofit.create(AirlinesRestApi::class.java)
    }

    private fun createOKHttp(): OkHttpClient {
        val okBuilder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(ReceiverInterceptor())

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okBuilder.addInterceptor(loggingInterceptor)
        }

        return okBuilder.build()
    }
}