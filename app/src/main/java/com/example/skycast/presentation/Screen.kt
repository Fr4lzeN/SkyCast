package com.example.skycast.presentation

sealed class Screen(val route: String) {
    data object MainScreen : Screen("main_screen")
    data object CityScreen : Screen("city_screen")
    data object ExtendedWeatherScreen : Screen("extended_weather_screen")
    data object WaitingScreen : Screen("waiting_screen")

    fun withArgs(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }
}