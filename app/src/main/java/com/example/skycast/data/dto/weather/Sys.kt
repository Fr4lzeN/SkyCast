package com.example.skycast.data.dto.weather

data class Sys(
    var type: Int,
    var id: Int,
    var country: String,
    var sunrise: Long,
    var sunset: Long,
)
