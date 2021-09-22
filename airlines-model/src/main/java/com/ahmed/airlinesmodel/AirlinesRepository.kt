package com.ahmed.airlinesmodel

import com.ahmed.airlinesmodel.entities.Airline

interface AirlinesRepository : AddingAirlineRepo, SearchingAirlineRepo {
    suspend fun getAllRemoteAirlines(): Result<ArrayList<Airline>?>
    suspend fun getAllLocalAirlines(): ArrayList<Airline>?
    suspend fun deleteAllLocalAirlines()
    suspend fun insertAllAirlines(airlines: List<Airline>)
}