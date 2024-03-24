package com.example.skycast.domain.model

import androidx.room.Embedded
import androidx.room.Relation
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


data class Forecast(
    @Embedded
    val city: City,
    @Relation(
        parentColumn = "city_id",
        entityColumn = "city_name",
        entity = DayWeather::class
    )
    val dayWeatherList: List<DayWeather>,
) {

    fun getForecastTime(): Calendar? {
        if (dayWeatherList.isEmpty()) return null
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = formatter.parse(dayWeatherList.first().forecastTime)!!
        return calendar
    }

}

