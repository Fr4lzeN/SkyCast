package com.example.skycast.data.dto.weather

import com.google.gson.annotations.SerializedName

data class CoordinatesDTO(
    @SerializedName("lat")
    var latitude: Double,
    @SerializedName("lon")
    var longitude: Double,
)
