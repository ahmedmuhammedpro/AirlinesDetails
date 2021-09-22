package com.ahmed.airlinesmodel

import com.ahmed.airlinesmodel.entities.Airline

interface AirlinesRepository : AddingAirlineRepo {
    suspend fun getAllRemoteAirlines(): Result<ArrayList<Airline>?>
    suspend fun getAllLocalAirlines(): ArrayList<Airline>?
    suspend fun deleteAllLocalAirlines()
    suspend fun getAirLineById(id: String): Result<Airline?>
    suspend fun getAirlineByName(name: String): Result<Airline?>
    suspend fun insertAllAirlines(airlines: List<Airline>)
}