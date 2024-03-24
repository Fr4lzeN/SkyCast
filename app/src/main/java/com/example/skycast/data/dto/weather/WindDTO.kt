package com.example.skycast.data.dto.weather

import com.google.gson.annotations.SerializedName

data class WindDTO(
    var speed: Double,
    @SerializedName("deg")
    var directionDegrees: Int,
    var gust: Double,
)
