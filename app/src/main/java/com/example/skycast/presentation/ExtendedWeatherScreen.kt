package com.example.skycast.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skycast.R
import com.example.skycast.domain.model.Forecast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ExtendedWeatherScreen(
    navController: NavHostController,
    selectedForecast: StateFlow<Forecast?>,
) {
    var isTextVisible by remember { mutableStateOf(true) }
    val forecast = selectedForecast.collectAsState()

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            isTextVisible = !isTextVisible
        }
    }

    val alpha by animateFloatAsState(
        targetValue = if (isTextVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing), label = ""
    )
    var offset by remember {
        mutableFloatStateOf(0f)
    }
    val size = with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
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
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = { offset = 0f },
                    onDragCancel = { offset = 0f }) { change, dragAmount ->
                    offset += dragAmount
                    if (offset > size * 0.1) {
                        offset = 0f
                        navController.popBackStack()
                    }
                }
            }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .alpha(alpha)
                .align(Alignment.CenterHorizontally)
                .clickable { navController.popBackStack() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrow_drop_up),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(text = "Обратно", color = Color.White, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(32.dp))
        Details(Modifier.fillMaxWidth(), forecast.value)
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun Details(modifier: Modifier = Modifier, forecast: Forecast?) {
    Column(modifier) {
        DelimiterRow(text = "Детали")
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.FixedSize(120.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            userScrollEnabled = false
        ) {
            items(7) {
                CreateDetailItem(it, forecast)
            }
        }
    }
}

@Composable
fun CreateDetailItem(index: Int, forecast: Forecast?) {
    val drawable: Int
    val name: String
    val value: String

    when (index) {
        0 -> {
            name = "Ощущается"
            value = "${forecast?.main?.feelsLikeTemperature}°"
            drawable = R.drawable.temperature
        }

        1 -> {
            name = "Ветер"
            value = "${forecast?.wind?.speed} км/ч"
            drawable = R.drawable.wind
        }

        2 -> {
            name = "Влажность"
            value = "${forecast?.main?.humidity}%"
            drawable = R.drawable.humidity
        }

        3 -> {
            name = "Давление"
            value = "${forecast?.main?.pressure} hPa"
            drawable = R.drawable.pressure
        }

        4 -> {
            name = "Видимость"
            value = "${forecast?.visibility} м"
            drawable = R.drawable.visibility
        }

        5 -> {
            name = "Шанс дождя"
            value = "${forecast?.pop}%"
            drawable = R.drawable.rain
        }

        else -> {
            name = "Облачность"
            value = "${forecast?.cloudiness}%"
            drawable = R.drawable.clouds
        }
    }
    DetailItem(
        Modifier
            .size(120.dp)
            .padding(8.dp),
        drawable = drawable,
        name = name,
        value = value
    )
}

@Composable
fun DelimiterRow(modifier: Modifier = Modifier, text: String) {
    Row(modifier) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Light,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .alpha(0.5f)
                .background(Color.White)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun DetailItem(modifier: Modifier = Modifier, drawable: Int, name: String, value: String) {
    Column(
        modifier.background(Color(66, 71, 122)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(id = drawable),
            contentDescription = null,
            Modifier.size(24.dp)
        )
        Text(text = name, color = Color.White, fontWeight = FontWeight.Light, fontSize = 16.sp)
        Text(text = value, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 20.sp)
    }
}

