package com.ahmed.airlinesmodel

import com.ahmed.airlinesmodel.entities.Airline
import com.ahmed.airlinesmodel.entities.ResponseState
import com.ahmed.airlinesmodel.local.AirlineDao
import com.ahmed.airlinesmodel.remote.AirlinesApi
import com.ahmed.airlinesmodel.remote.AirlinesRestApi

class AirlinesRepositoryImp(private val airlineDao: AirlineDao) : AirlinesRepository {

    private val airlinesRestApi: AirlinesRestApi by lazy {
        AirlinesApi.getAirlineRestApi()
    }

    override suspend fun getAllRemoteAirlines(): Result<ArrayList<Airline>?> {
        return try {
            val airlines = airlinesRestApi.getAirlines()
            when (airlines.getResponseState()) {
                ResponseState.SUCCESS -> Result.success(airlines.map())
                else -> Result.failure(Exception("network error"))
            }
        } catch (ex: Throwable) {
            Result.failure(ex)
        }
    }

    override suspend fun getAllLocalAirlines(): ArrayList<Airline>? {
        val airlines =  airlineDao.getAllAirlines()
        return if (!airlines.isNullOrEmpty()) {
            ArrayList(airlines)
        } else {
            null
        }
    }

    override suspend fun deleteAllLocalAirlines() {
        airlineDao.deleteAllAirlines()
    }

    override suspend fun getAirLineById(id: String): Result<Airline?> {
        return try {
            val airline = airlinesRestApi.getAirLineById(id)
            when (airline.getResponseState()) {
                ResponseState.SUCCESS -> Result.success(airline.map())
                else -> Result.failure(Exception("network error"))
            }
        } catch (ex: Throwable) {
            Result.failure(ex)
        }
    }

    override suspend fun getAirlineByName(name: String): Result<Airline?> {
        return try {
            val airlinesResponse = airlinesRestApi.getAirlines()
            when (airlinesResponse.getResponseState()) {
                ResponseState.SUCCESS -> {
                    val airline = airlinesResponse.map().find { it.name == name }
                    Result.success(airline)
                }
                else -> Result.failure(Exception("network error"))
            }
        } catch (ex: Throwable) {
            Result.failure(ex)
        }
    }

    override suspend fun insertAllAirlines(airlines: List<Airline>) {
        airlineDao.insertAllAirlines(airlines)
    }

    override suspend fun addAirline(airline: Airline): Result<ArrayList<Airline>?> {
        return try {
            val response = airlinesRestApi.addAirline(airline)
            when (response.getResponseState()) {
                ResponseState.SUCCESS -> {
                    airlineDao.insertAirline(airline)
                    val airlines = airlineDao.getAllAirlines()
                    var airlinesArrayList: ArrayList<Airline>? = null
                    if (!airlines.isNullOrEmpty()) {
                        airlinesArrayList = ArrayList(airlines)
                    }
                    Result.success(airlinesArrayList)
                }
                else -> Result.failure(Exception("network error"))
            }
        } catch (ex: Throwable) {
            Result.failure(ex)
        }
    }
}
