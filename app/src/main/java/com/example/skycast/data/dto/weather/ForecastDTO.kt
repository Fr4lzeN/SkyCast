package com.example.skycast.data.dto.weather

import com.google.gson.annotations.SerializedName

data class ForecastDTO(
    @SerializedName("list")
    var dayWeatherList: List<DayWeatherDTO>,
    var city: CityDTO,
)
