package com.example.skycast.di

import android.app.Application
import androidx.room.Room
import com.example.skycast.data.data_source.api.WeatherApi
import com.example.skycast.data.data_source.database.ForecastDatabase
import com.example.skycast.data.repository.ForecastRepositoryImpl
import com.example.skycast.data.repository.WeatherApiRepositoryImpl
import com.example.skycast.domain.repository.ForecastRepository
import com.example.skycast.domain.repository.WeatherApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideForecastDatabase(app: Application): ForecastDatabase{
        return Room.databaseBuilder(
            app.applicationContext,
            ForecastDatabase::class.java,
            ForecastDatabase.DATABASE_NAME,
            ).build()
    }

    @Singleton
    @Provides
    fun provideForecastRepository(database: ForecastDatabase): ForecastRepository{
        return ForecastRepositoryImpl(database.forecastDao)
    }

    @Singleton
    @Provides
    fun provideWeatherApi(): WeatherApi{
        return Retrofit.Builder().baseUrl(WeatherApi.BASE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(WeatherApi::class.java)
    }

    @Singleton
    @Provides
    fun provideWeatherApiRepository(weatherApi: WeatherApi): WeatherApiRepository{
        return WeatherApiRepositoryImpl(weatherApi)
    }
}