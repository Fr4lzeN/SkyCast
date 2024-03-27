package com.example.skycast.core

import com.example.skycast.R
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
    THUNDERSTORM("Thunderstorm");

}

fun iconFromWeatherType(
    weatherType: WeatherType?,
    sunrise: Long? = null,
    sunset: Long? = null
): Int {
    if (weatherType == null) return R.drawable.clear
    return when (weatherType) {
        WeatherType.CLEAR ->
            if (sunrise == null || sunset == null || System.currentTimeMillis() / 1000 in sunrise..sunset) {
                R.drawable.clear
            } else {
                R.drawable.clear_night
            }
        WeatherType.RAIN -> R.drawable.rain_icon
        WeatherType.SNOW -> R.drawable.snow
        WeatherType.CLOUDS -> R.drawable.cloud_icon
        WeatherType.DRIZZLE -> R.drawable.drizzle
        WeatherType.THUNDERSTORM -> R.drawable.thunderstorm
    }
}