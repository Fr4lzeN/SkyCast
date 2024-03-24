package com.example.skycast.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.DayWeather
import com.example.skycast.domain.model.Forecast

@Dao
abstract class ForecastDao {

    suspend fun addForecast(forecast: Forecast){
        addCity(forecast.city)
        forecast.dayWeatherList.forEach {
            it.id=addDayWeather(it)
        }
    }

    suspend fun updateForecast(forecast: Forecast){
            updateDayWeather(forecast.dayWeatherList)
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addCity(city: City)

    @Transaction
    @Delete
    abstract suspend fun deleteCity(city: City)

    @Insert
    abstract suspend fun addDayWeather(dayWeatherList: DayWeather): Long

    @Query("SELECT * FROM city")
    abstract suspend fun getCities(): List<City>

    @Transaction
    @Query("SELECT * FROM city WHERE city_id = :cityName")
    abstract suspend fun getForecast(cityName: String): Forecast

    @Update
    @JvmSuppressWildcards
    abstract suspend fun updateDayWeather(dayWeather: List<DayWeather>)



}