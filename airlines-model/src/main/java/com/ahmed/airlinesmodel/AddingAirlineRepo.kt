package com.ahmed.airlinesmodel

import com.ahmed.airlinesmodel.entities.Airline

interface AddingAirlineRepo {
    suspend fun addAirline(airline: Airline): Result<ArrayList<Airline>?>
}