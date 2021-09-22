package com.ahmed.airlinesmodel.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmed.airlinesmodel.entities.Airline

@Database(entities = [Airline::class], version = 1, exportSchema = false)
abstract class AirlineDatabase : RoomDatabase() {

    abstract val airlineDao: AirlineDao

    companion object {
        private var INSTANCE: AirlineDatabase? = null
        fun getDatabase(context: Context): AirlineDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AirlineDatabase::class.java,
                    "airline_db"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }

}