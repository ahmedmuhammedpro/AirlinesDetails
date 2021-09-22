package com.ahmed.airlinesmodel.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmed.airlinesmodel.entities.Airline

@Dao
interface AirlineDao {
    @Query("SELECT * FROM AIRLINE")
    suspend fun getAllAirlines(): List<Airline>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAirline(dbAirline: Airline)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllAirlines(airlines: List<Airline>)

    @Query("DELETE FROM AIRLINE")
    suspend fun deleteAllAirlines()
}