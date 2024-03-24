package com.example.skycast.data.dto.weather

import com.google.gson.annotations.SerializedName

data class CityDTO(
    var name: String,
    var sunrise: Long,
    var sunset: Long,
    @SerializedName("cord")
    var coordinates: CoordinatesDTO
)
