package com.example.skycast.presentation

import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skycast.R
import com.example.skycast.core.WeatherType
import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.DayWeather
import com.example.skycast.domain.model.Forecast
import com.example.skycast.domain.model.MainWeather
import com.example.skycast.domain.model.Weather
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import java.util.Calendar
import kotlin.math.abs


@Composable
fun WeatherScreen(
    navController: NavController,
    forecasts: StateFlow<List<Forecast>?>,
    selected: Int
) {
    var offset by remember {
        mutableFloatStateOf(0f)
    }

    var selectedForecast by remember {
        mutableStateOf<Forecast?>(null)
    }

    var forecastList by remember {
        mutableStateOf<List<Forecast>?>(null)
    }

    LaunchedEffect(forecasts) {
        forecasts
            .filterNotNull()
            .collectLatest { list ->
                selectedForecast = selectedForecast?.let {
                    list.find { forecast ->
                        selectedForecast?.cityName == forecast.cityName
                    }
                } ?: list.getOrNull(selected)
                forecastList = list
            }
    }
    val size = with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = { offset = 0f },
                    onDragCancel = { offset = 0f }) { change, dragAmount ->
                    offset += dragAmount
                    Log.d("swipe", offset.toString())
                    if (offset < 0 && abs(offset) > size * 0.1) {
                        offset = 0f
                        navController.navigate(Screen.ExtendedWeatherScreen.withArgs(selected.toString()))
                    }
                }
            }
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(29, 38, 87),
                        Color(29, 38, 87),
                        Color(148, 98, 158)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                selectedForecast?.cityName ?: "",
            ) { navController.navigate(Screen.CityScreen.route){
                popUpTo(Screen.MainScreen.route+"/${selected}") {
                    inclusive = true
                }
            } }
            Spacer(modifier = Modifier.height(8.dp))
            CityCount(Modifier.align(Alignment.CenterHorizontally), selectedForecast, forecastList)
        }
        TemperatureInfo(
            Modifier
                .align(Alignment.CenterStart),
            selectedForecast?.weather,
            selectedForecast?.main,
            selectedForecast?.cityInfo
        )
        ShortForecast(Modifier.align(Alignment.BottomCenter))
        { navController.navigate(Screen.ExtendedWeatherScreen.withArgs(selected.toString())) }
    }
}

@Composable
fun ShortForecast(
    modifier: Modifier = Modifier,
    onDetailedClick: () -> Unit
) {
    var isTextVisible by remember { mutableStateOf(true) }

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
    Column(
        modifier = modifier
            .alpha(alpha)
            .clickable { onDetailedClick() }
    ) {
        Text(text = "Подробнее", color = Color.White, fontSize = 14.sp)
        Image(
            painter = painterResource(id = R.drawable.arrow_drop_down),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }

}

@Composable
fun WeatherItem(modifier: Modifier = Modifier, dayWeather: DayWeather?, index: Int) {
    Row(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Row {
            Image(
                painter = painterResource(
                    id = iconFromWeatherType(
                        dayWeather?.weather?.type,
                        null,
                        null
                    )
                ),
                contentDescription = null,
                modifier = Modifier.align(
                    Alignment.CenterVertically
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = dayWeather.dayOfWeak(index),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.align(
                    Alignment.CenterVertically
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = dayWeather?.weather?.description ?: "",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.align(
                    Alignment.CenterVertically
                )
            )
        }

        Text(
            text = "${dayWeather?.mainWeather?.temperatureMin}°С/${dayWeather?.mainWeather?.temperatureMax}°С",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.align(
                Alignment.CenterVertically
            )
        )
    }

}

private fun DayWeather?.dayOfWeak(index: Int): String {
    if (this == null) return ""
    val date = Calendar.getInstance()
    return when ((date.get(Calendar.DAY_OF_WEEK) - 1 + index) % 7) {
        0 -> "Пн"
        1 -> "Вт"
        2 -> "Ср"
        3 -> "Чт"
        4 -> "Пт"
        5 -> "Сб"
        else -> "Вс"
    }
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    cityName: String,
    onMenuClick: () -> Unit
) {
    Row(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Image(
            painter = painterResource(id = R.drawable.baseline_dehaze_24),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Bottom)
                .clickable {
                    onMenuClick()
                }
        )
        Text(
            text = cityName,
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Light
        )
        Image(
            painter = painterResource(id = R.drawable.icons_settings),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Bottom)
                .alpha(0f)
        )
    }
}

@Composable
fun CityCount(modifier: Modifier, selectedForecast: Forecast?, forecastList: List<Forecast>?) {
    LazyRow(modifier = modifier, content = {
        items(forecastList?.size ?: 0) {
            Box(
                Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(
                        if (forecastList?.getOrNull(it) == selectedForecast) Color.Yellow else Color.White
                    )
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    })
}

fun iconFromWeatherType(weatherType: WeatherType?, sunrise: Long?, sunset: Long?): Int {
    if (weatherType == null) return R.drawable.clear
    return when (weatherType) {
        WeatherType.CLEAR ->
            if (sunrise == null || sunset == null || System.currentTimeMillis() in sunrise..sunset) {
                R.drawable.clear
            } else {
                R.drawable.clear_night
            }

        WeatherType.RAIN -> R.drawable.rain
        WeatherType.SNOW -> R.drawable.snow
        WeatherType.CLOUDS -> R.drawable.clouds
        WeatherType.DRIZZLE -> R.drawable.drizzle
        WeatherType.THUNDERSTORM -> R.drawable.thunderstorm
    }
}

@Composable
fun TemperatureInfo(
    modifier: Modifier = Modifier,
    weather: Weather?,
    mainWeather: MainWeather?,
    city: City?
) {
    Column(modifier.fillMaxWidth()) {
        Row {
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(
                    id = iconFromWeatherType(weather?.type, city?.sunrise, city?.sunset)
                ),
                contentDescription = null,
                Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = weather?.description ?: "",
                Modifier.align(Alignment.CenterVertically),
                fontSize = 40.sp,
                color = Color.White,
                fontWeight = FontWeight.Light
            )
        }
        Row {
            Text(
                text = (mainWeather?.temperature ?: "").toString(),
                fontWeight = FontWeight.Light,
                color = Color.White,
                fontSize = 180.sp
            )
            Text(
                text = "°",
                color = Color.White,
                fontSize = 120.sp,
                fontWeight = FontWeight.W300
            )
            Column(
                Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .wrapContentWidth()
            ) {
                Text(
                    text = "${(mainWeather?.temperatureMax ?: "")}°C",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.W300,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Box(
                    Modifier
                        .height(1.dp)
                        .width(128.dp)
                        .background(Color.White)
                )
                Text(
                    text = "${(mainWeather?.temperatureMin ?: "")}°C",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.W300,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}


