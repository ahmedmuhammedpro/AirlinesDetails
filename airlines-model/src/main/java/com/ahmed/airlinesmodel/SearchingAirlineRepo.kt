package com.ahmed.airlinesmodel

import com.ahmed.airlinesmodel.entities.Airline

interface SearchingAirlineRepo {
    suspend fun getAirLineById(id: String): Result<Airline?>
    suspend fun getAirlineByName(name: String): Result<Airline?>
}