package com.example.skycast.data.data_source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skycast.domain.model.Forecast

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: Forecast): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertAllForecasts(forecasts: List<Forecast>)

    @Query("SELECT * FROM forecast WHERE cityName==:cityName")
    suspend fun getForecast(cityName: String): Forecast

    @Query("SELECT * FROM forecast ORDER BY date asc")
    suspend fun getAllForecasts(): List<Forecast>

    @Query("DELETE FROM forecast WHERE cityName==:cityName")
    suspend fun deleteForecast(cityName: String)

}