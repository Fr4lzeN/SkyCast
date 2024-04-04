package com.example.skycast.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skycast.R
import com.example.skycast.core.SwipeDirection
import com.example.skycast.core.iconFromWeatherType
import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.Forecast
import com.example.skycast.domain.model.MainWeather
import com.example.skycast.domain.model.Weather
import com.example.skycast.presentation.Screen
import com.example.skycast.presentation.ui.AlphaAnimationScope
import com.example.skycast.presentation.ui.AnimateHorizontalSlide
import com.example.skycast.presentation.view_models.WeatherViewModel
import java.util.Locale
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    navController: NavController,
    viewModel: WeatherViewModel,

    ) {
    var horizontalOffset by remember { mutableFloatStateOf(0f) }
    val forecastList = viewModel.forecasts.collectAsState()
    val selectedForecast = viewModel.selectedForecast.collectAsState()
    var swipeDirection by remember { mutableStateOf(SwipeDirection.RIGHT) }
    val sizeHorizontal = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    Scaffold(modifier = Modifier
        .pointerInput(Unit) {
            detectHorizontalDragGestures(
                onDragCancel = {
                    horizontalOffset = 0f
                },
                onDragEnd = {
                    calculateSwipeDirection(
                        horizontalOffset,
                        sizeHorizontal
                    )?.let { direction ->
                        swipeDirection = direction
                        viewModel.switchForecast(
                            selectedForecast.value,
                            direction == SwipeDirection.RIGHT
                        )
                    }
                    horizontalOffset = 0f
                },
                onHorizontalDrag = { _, dragAmount ->
                    horizontalOffset += dragAmount
                })
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    AnimateHorizontalSlide(
                        modifier = Modifier.fillMaxWidth(),
                        state = selectedForecast.value,
                        swipeDirection = swipeDirection
                    ) {
                        Text(
                            text = it?.cityName ?: "",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.CityScreen.route) {
                            popUpTo(Screen.MainScreen.route) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_dehaze_24),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(29, 38, 87),
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White
                )
            )
        }) { innerPadding ->
        Column(
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
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            CityCount(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                selectedForecast = selectedForecast.value,
                forecastList = forecastList.value
            )
            AnimateHorizontalSlide(
                state = selectedForecast.value,
                swipeDirection = swipeDirection
            ) {
                TemperatureInfo(
                    weather = it?.weather,
                    mainWeather = it?.main,
                    city = it?.cityInfo
                )
            }
            AlphaAnimationScope(Modifier.align(Alignment.CenterHorizontally)) {
                ExtendWeather(Modifier.clickable
                { navController.navigate(Screen.ExtendedWeatherScreen.route) })
            }
        }
    }
}

fun calculateSwipeDirection(horizontalOffset: Float, sizeHorizontal: Float): SwipeDirection? {
    if (abs(horizontalOffset) > sizeHorizontal * 0.15) {
        return if (horizontalOffset < 0) SwipeDirection.RIGHT else SwipeDirection.LEFT
    }
    return null
}

@Composable
fun ExtendWeather(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
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

@OptIn(ExperimentalFoundationApi::class)
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
                text = weather?.description?.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                    ?: "",
                maxLines = 1,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .basicMarquee(),
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