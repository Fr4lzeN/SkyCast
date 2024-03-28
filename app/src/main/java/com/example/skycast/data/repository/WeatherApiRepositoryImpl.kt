package com.example.skycast.data.repository

import com.example.skycast.data.data_source.api.WeatherApi
import com.example.skycast.data.dto.weather.ForecastDTO
import com.example.skycast.data.dto.weather.MainWeatherDTO
import com.example.skycast.data.dto.weather.WeatherDTO
import com.example.skycast.data.dto.weather.WindDTO
import com.example.skycast.domain.model.City
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
    ): Result<Forecast> {
        val response =
            weatherApi.getWeather(
                cityName, "dc8ecec968c5f0fa2162b50eb1cce678",
                "metric",
                "ru"
            )
        if (response.isSuccessful) {
            return Result.success(response.body()!!.toDomain())
        }
        return Result.failure(Throwable(response.message()))
    }

    override suspend fun getForecast(
        longitude: Double,
        latitude: Double,
    ): Result<Forecast> {
        val response = weatherApi.getWeather(
            latitude,
            longitude,
            "dc8ecec968c5f0fa2162b50eb1cce678",
            "metric",
            "ru"
        )
        if (response.isSuccessful) {
            return Result.success(response.body()!!.toDomain())
        }
        return Result.failure(Throwable(response.message()))
    }

    private fun ForecastDTO.toDomain(): Forecast {
        return Forecast(
            date = System.currentTimeMillis(),
            main = this.main.toDomain(),
            weather = this.weather[0].toDomain(),
            cloudiness = clouds.cloudiness,
            wind = wind.toDomain(),
            visibility = visibility,
            pop = pop,
            cityName = name,
            cityInfo = City(sunrise = sys.sunrise, sunset = sys.sunset)
        )
    }

    private fun MainWeatherDTO.toDomain(): MainWeather {
        return MainWeather(
            kotlin.math.round(temperature).toInt(),
            kotlin.math.round(feelsLikeTemperature).toInt(),
            kotlin.math.round(temperatureMin).toInt(),
            kotlin.math.round(temperatureMax).toInt(),
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
}