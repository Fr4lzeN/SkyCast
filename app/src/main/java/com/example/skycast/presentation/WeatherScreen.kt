package com.example.skycast.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.skycast.core.iconFromWeatherType
import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.Forecast
import com.example.skycast.domain.model.MainWeather
import com.example.skycast.domain.model.Weather
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.abs


@Composable
fun WeatherScreen(
    navController: NavController,
    forecasts: StateFlow<List<Forecast>?>,
    selected: StateFlow<Forecast?>,
    switchForecast: (Forecast?, Boolean) -> Unit
) {
    var horizontalOffset by remember { mutableFloatStateOf(0f) }
    val forecastList = forecasts.collectAsState()
    val selectedForecast = selected.collectAsState()
    val sizeHorizontal = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
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
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = { horizontalOffset = 0f },
                    onDragCancel = { horizontalOffset = 0f },
                    onHorizontalDrag = { change, dragAmount ->
                        horizontalOffset += dragAmount
                        if (abs(horizontalOffset) > sizeHorizontal * 0.15) {
                            if (horizontalOffset > 0) {
                                switchForecast(selectedForecast.value, false)
                            } else {
                                switchForecast(selectedForecast.value, true)
                            }
                            horizontalOffset = 0f
                        }
                    })
            }
    ) {
        Column {

            TopBar(
                modifier = Modifier.fillMaxWidth(),
                cityName = selectedForecast.value?.cityName ?: "",
                onMenuClick = {
                    navController.navigate(Screen.CityScreen.route) {
                        popUpTo(Screen.MainScreen.route) {
                            inclusive = true
                        }
                    }
                })

            Spacer(modifier = Modifier.height(8.dp))

            CityCount(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                selectedForecast = selectedForecast.value,
                forecastList = forecastList.value
            )

        }

        TemperatureInfo(
            modifier = Modifier
                .align(Alignment.CenterStart),
            weather = selectedForecast.value?.weather,
            mainWeather = selectedForecast.value?.main,
            city = selectedForecast.value?.cityInfo
        )

        ShortForecast(
            modifier = Modifier.align(Alignment.BottomCenter),
            onDetailedClick = { navController.navigate(Screen.ExtendedWeatherScreen.route) })

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

    val state = rememberLazyListState()

    LazyRow(modifier = modifier, state = state, content = {
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


