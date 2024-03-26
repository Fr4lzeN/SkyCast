package com.example.skycast.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation() {

    val viewModel = hiltViewModel<WeatherViewModel>()

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.WaitingScreen.route) {

        composable(route = Screen.WaitingScreen.route) {
            viewModel.getForecast()
            WaitingScreen(navController, viewModel.hasCities)
        }

        composable(route = Screen.MainScreen.route + "/{selected}",
            arguments = listOf(
                navArgument("selected") {
                    type = NavType.IntType
                    defaultValue = 0
                    nullable = false
                }
            )) {
            WeatherScreen(
                navController = navController,
                viewModel.forecasts,
                it.arguments!!.getInt("selected")
            )
        }
        // how to use arguments
        /*composable(route = Screen.CityScreen.route+"/{city}",
            arguments = listOf(
                navArgument("city") {
                    type = NavType.StringType
                    defaultValue = "Moscow"
                    nullable = false
                }
            )) {
            CityScreen(city = it.arguments?.getString("city")!!)
        }*/
        composable(route = Screen.CityScreen.route) {
            CityScreen(
                navController = navController,
                viewModel.forecasts,
                viewModel.errorMessage,
                viewModel::addCity
            )
        }
        composable(route = Screen.ExtendedWeatherScreen.route + "/{selected}",
            arguments = listOf(
                navArgument("selected") {
                    type = NavType.IntType
                    defaultValue = 0
                    nullable = false
                }
            )) {
            ExtendedWeatherScreen(navController = navController, viewModel.forecasts,it.arguments!!.getInt("selected"))
        }
    }
}


