package com.example.skycast.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.skycast.presentation.Screen
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun WaitingScreen(navController: NavHostController, hasCities: SharedFlow<Boolean?>) {
    LaunchedEffect(hasCities) {
        hasCities.filterNotNull().collect {
            if (it) {
                navController.navigate(Screen.MainScreen.route) {
                    popUpTo(Screen.WaitingScreen.route) {
                        inclusive = true
                    }
                }
            } else {
                navController.navigate(Screen.CityScreen.route) {
                    popUpTo(Screen.WaitingScreen.route) {
                        inclusive = true
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(29, 38, 87),
                        Color(148, 98, 158)
                    )
                )
            )
            .padding(16.dp),
    ) {
        CircularProgressIndicator(
            Modifier
                .align(Alignment.Center)
                .size(128.dp)
        )
    }
}