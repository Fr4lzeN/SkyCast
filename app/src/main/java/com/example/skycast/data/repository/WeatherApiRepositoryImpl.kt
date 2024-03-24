package com.example.skycast.data.repository

import com.example.skycast.data.data_source.api.WeatherApi
import com.example.skycast.data.dto.weather.CityDTO
import com.example.skycast.data.dto.weather.DayWeatherDTO
import com.example.skycast.data.dto.weather.ForecastDTO
import com.example.skycast.data.dto.weather.MainWeatherDTO
import com.example.skycast.data.dto.weather.WeatherDTO
import com.example.skycast.data.dto.weather.WindDTO
import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.DayWeather
import com.example.skycast.domain.model.Forecast
import com.example.skycast.domain.model.MainWeather
import com.example.skycast.domain.model.Weather
import com.example.skycast.domain.model.Wind
import com.example.skycast.domain.repository.WeatherApiRepository

class WeatherApiRepositoryImpl(
    private val weatherApi: WeatherApi,
) : WeatherApiRepository {
    override suspend fun getForecast(
        cityName: String,
        apikey: String,
        units: String,
        language: String
    ): Result<Forecast> {
        val response = weatherApi.getWeather(cityName, apikey, units, language)
        if (response.isSuccessful) {
            return Result.success(response.body()!!.toDomain())
        }
        return Result.failure(Throwable(response.message()))
    }

    override suspend fun getForecast(
        longitude: Double,
        latitude: Double,
        apikey: String,
        units: String,
        language: String
    ): Result<Forecast> {
        val response = weatherApi.getWeather(latitude, longitude, apikey, units, language)
        if (response.isSuccessful) {
            return Result.success(response.body()!!.toDomain())
        }
        return Result.failure(Throwable(response.message()))
    }


    // настроить appModule
    //сделать App
    // сохранить apikey
    //


    private fun ForecastDTO.toDomain(): Forecast {
        val city = city.toDomain()
        return Forecast(city, dayWeatherList.map { it.toDomain(city.name) })
    }

    private fun CityDTO.toDomain(): City {
        return City(name, sunrise, sunset)
    }
}

private fun DayWeatherDTO.toDomain(cityId: String): DayWeather {
    return DayWeather(
        cityId = cityId,
        date = date,
        cloudiness = clouds.cloudiness,
        wind = wind.toDomain(),
        visibility = visibility,
        pop = pop,
        weather = weather.toDomain(),
        mainWeather = main.toDomain(),
        forecastTime = forecastTime
    )
}

private fun MainWeatherDTO.toDomain(): MainWeather {
    return MainWeather(
        temperature,
        feelsLikeTemperature,
        temperatureMin,
        temperatureMax,
        pressure,
        humidity
    )
}

private fun WeatherDTO.toDomain(): Weather {
    return Weather(type, description)
}

private fun WindDTO.toDomain(): Wind {
    return Wind(speed, directionDegrees, gust)
}
