package com.example.skycast.core

import com.google.gson.annotations.SerializedName

enum class WeatherType(s: String) {

    @SerializedName("Rain")
    RAIN("Rain"),
    @SerializedName("Snow")
    SNOW("Snow"),
    @SerializedName("Clouds")
    CLOUDS("Clouds"),
    @SerializedName("Clear")
    CLEAR("Clear"),
    @SerializedName("Drizzle")
    DRIZZLE("Drizzle"),
    @SerializedName("Thunderstorm")
    THUNDERSTORM("Thunderstorm"),

}