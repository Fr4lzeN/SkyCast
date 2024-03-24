package com.example.skycast.domain.model

import com.example.skycast.core.WeatherType

data class Weather(
    val type: WeatherType,
    val description: String,
)
