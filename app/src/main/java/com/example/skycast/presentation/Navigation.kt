package com.example.skycast.presentation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {

    val viewModel = hiltViewModel<WeatherViewModel>()

    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = Screen.WaitingScreen.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {

        composable(route = Screen.WaitingScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            }) {
            viewModel.getForecast()
            WaitingScreen(navController, viewModel.hasCities)
        }

        composable(route = Screen.MainScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            }) {
            WeatherScreen(
                navController = navController,
                viewModel.forecasts,
                viewModel.selectedForecast,
                viewModel::switchForecast
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
        composable(route = Screen.CityScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            }) {
            CityScreen(
                navController = navController,
                viewModel.forecasts,
                viewModel.errorMessage,
                viewModel::addCity,
                viewModel::deleteForecast,
                viewModel::selectForecast,
            )
        }
        composable(route = Screen.ExtendedWeatherScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            }) {
            ExtendedWeatherScreen(navController = navController, viewModel.selectedForecast)
        }
    }
}


