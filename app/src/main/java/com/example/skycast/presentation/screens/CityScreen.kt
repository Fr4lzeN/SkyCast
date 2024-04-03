package com.example.skycast.presentation.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skycast.R
import com.example.skycast.domain.model.Forecast
import com.example.skycast.domain.model.MainWeather
import com.example.skycast.presentation.Screen
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityScreen(
    navController: NavHostController,
    forecasts: StateFlow<List<Forecast>?>,
    errorMessage: SharedFlow<String>,
    addCity: (String) -> Unit,
    deleteCity: (Forecast) -> Unit,
    selectCity: (Forecast) -> Unit
) {

    val forecastsList = forecasts.collectAsState()
    val error = errorMessage.collectAsState(initial = null)
    ShowError(error.value)

    Column(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(29, 38, 87),
                        Color(148, 98, 158)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Управление городами",
            Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        SearchCity(Modifier.fillMaxWidth(), addCity)
        Spacer(modifier = Modifier.height(32.dp))
        LazyColumn {
            items(
                items = forecastsList.value ?: emptyList<Forecast>(),
                key = { it.cityName }) { forecast ->
                val dismiss = rememberDismissState(
                    DismissValue.Default, positionalThreshold = {
                        it * 0.4f
                    }, confirmValueChange = { dismissValue ->
                        if (dismissValue == DismissValue.DismissedToStart) {
                            deleteCity(forecast)
                        }
                        true
                    }
                )
                SwipeToDismiss(state = dismiss, background = {
                    val color =
                        if (dismiss.targetValue == DismissValue.DismissedToStart) Color.White else Color(
                            255,
                            255,
                            255,
                            200
                        )
                    val imageColor =
                        if (dismiss.targetValue == DismissValue.DismissedToStart) Color.Black else Color(
                            0,
                            0,
                            0,
                            200
                        )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color, RoundedCornerShape(16.dp))
                            .padding(8.dp)
                    ) {
                        Image(
                            painterResource(id = R.drawable.baseline_delete_forever_24),
                            null,
                            Modifier
                                .size(32.dp)
                                .align(
                                    Alignment.CenterEnd
                                ),
                            colorFilter = ColorFilter.tint(imageColor)
                        )
                    }
                }, dismissContent = {
                    CityInfo(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectCity(forecast)
                                navController.navigate(Screen.MainScreen.route) {
                                    popUpTo(Screen.CityScreen.route) {
                                        inclusive = true
                                    }
                                }
                            },
                        forecast.cityName,
                        forecast.main
                    )
                }, directions = setOf(DismissDirection.EndToStart))

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ShowError(message: String?) {
    var haveDismiss by remember {
        mutableStateOf(false)
    }
    if (haveDismiss) return
    if (message != null) {
        AlertDialog(
            onDismissRequest = { haveDismiss = true },
            confirmButton = {},
            title = { Text(text = "Ошибка") },
            text = { Text(text = message) },
            dismissButton = {
                TextButton(onClick = { haveDismiss = true }) {
                    Text(text = "Закрыть")
                }
            }
        )
    }
}

@Composable
fun CityInfo(modifier: Modifier, cityName: String, weather: MainWeather) {
    Row(
        modifier
            .background(
                Color(66, 71, 122),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = cityName,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light
            )
            Text(
                text = "${weather.temperatureMin}°C/${weather.temperatureMax}°C",
                color = Color.White,
                fontWeight = FontWeight.Light
            )
        }
        Text(
            text = "${weather.temperature}°",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun SearchCity(modifier: Modifier, addCity: (String) -> Unit) {
    var cityName by remember { mutableStateOf("") }
    OutlinedTextField(
        value = cityName, onValueChange = { cityName = it },
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(8.dp),
        textStyle = TextStyle(color = Color.White),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color(255, 255, 255, 200),
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color(255, 255, 255, 200),
            focusedPlaceholderColor = Color.White,
            unfocusedPlaceholderColor = Color(255, 255, 255, 200),
        ),
        label = { Text(text = "Город") },
        placeholder = { Text(text = "Введите название города") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            addCity(cityName)
        }),
    )
}