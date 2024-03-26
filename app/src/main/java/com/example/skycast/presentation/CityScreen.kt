package com.example.skycast.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skycast.domain.model.Forecast
import com.example.skycast.domain.model.MainWeather
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun CityScreen(
    navController: NavHostController,
    forecasts: StateFlow<List<Forecast>?>,
    errorMessage: SharedFlow<String>,
    addCity: (String) -> Unit
) {

    var forecastsList by remember {
        mutableStateOf<List<Forecast>?>(null)
    }

    val error = errorMessage.collectAsState(initial = null)

    ShowError(error.value)

    LaunchedEffect(forecasts) {
        forecasts
            .filterNotNull()
            .collectLatest {
                forecastsList = it
            }
    }

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
        /*Image(
            painter = painterResource(id = R.drawable.arrow_back),
            contentDescription = null,
            Modifier
                .size(32.dp)
                .clickable { navController.popBackStack() }
        )*/
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
            items(forecastsList?.size ?: 0) {
                CityInfo(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screen.MainScreen.withArgs(it.toString())) {
                                popUpTo(Screen.CityScreen.route) {
                                    inclusive = true
                                }
                            }
                        },
                    forecastsList!![it].cityName,
                    forecastsList!![it].main
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCity(modifier: Modifier, addCity: (String) -> Unit) {
    var cityName by remember { mutableStateOf("") }
    OutlinedTextField(
        value = cityName, onValueChange = { cityName = it },
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(8.dp),
        textStyle = TextStyle(color = Color.White),
        colors = TextFieldDefaults.outlinedTextFieldColors(
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

/*
@Preview
@Composable
fun PreviewCity() {
    CityScreen(rememberNavController(), )
}*/
