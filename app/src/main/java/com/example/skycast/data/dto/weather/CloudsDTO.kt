package com.example.skycast.data.dto.weather

import com.google.gson.annotations.SerializedName

data class CloudsDTO(
    @SerializedName("all")
    var cloudiness: Int
)
