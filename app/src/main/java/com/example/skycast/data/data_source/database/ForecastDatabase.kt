package com.example.skycast.data.data_source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.skycast.data.data_source.dao.ForecastDao
import com.example.skycast.domain.model.Forecast
import javax.inject.Singleton

@Singleton
@Database(
    entities =[Forecast::class],
    version = 1,
    exportSchema = false
)
abstract class ForecastDatabase: RoomDatabase(){

    abstract val forecastDao: ForecastDao

    companion object{
        val DATABASE_NAME = "city"
    }
}
