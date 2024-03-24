package com.example.skycast.data.dto.weather

import com.example.skycast.core.WeatherType
import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    var id: Int,
    @SerializedName("main")
    var type: WeatherType,
    var description: String,
)
