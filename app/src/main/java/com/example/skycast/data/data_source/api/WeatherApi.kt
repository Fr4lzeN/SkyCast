package com.example.skycast.data.data_source.api

import com.example.skycast.data.dto.weather.ForecastDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    companion object {
        val BASE_API = "https://api.openweathermap.org/"
    }

    @GET("data/2.5/forecast")
    fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apikey: String,
        @Query("units") units: String, // standard, metric and imperial
        @Query("lang") language: String,
    ): Response<ForecastDTO>

    @GET("data/2.5/forecast")
    fun getWeather(
        @Query("q") city: String,
        @Query("appid") apikey: String,
        @Query("units") units: String, // standard, metric and imperial
        @Query("lang") language: String,
    ): Response<ForecastDTO>

}