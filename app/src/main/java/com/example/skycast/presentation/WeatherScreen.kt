package com.example.skycast.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.R
import kotlinx.coroutines.delay


@Composable
fun WeatherScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(29, 38, 87),
                    Color(148, 98, 158))
                )
            )
            .padding(16.dp)
    ) {
        Column {
            TopBar()
            Spacer(modifier = Modifier.height(8.dp))
            CityCount(Modifier.align(Alignment.CenterHorizontally))
        }
        TemperatureInfo(
            Modifier
                .align(Alignment.CenterStart)
        )
        ShortForecast(Modifier.align(Alignment.BottomStart))
    }
}

@Composable
fun ShortForecast(modifier: Modifier = Modifier) {
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
    Column(modifier) {
        LazyColumn(modifier = modifier, content = {
            items(3) {
                WeatherItem(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        })
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .alpha(alpha)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Подробнее", color = Color.White, fontSize = 14.sp)
            Image(
                painter = painterResource(id = R.drawable.arrow_drop_down),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

    }
}

@Composable
fun WeatherItem(modifier: Modifier = Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.clear_night),
                contentDescription = null,
                modifier = Modifier.align(
                    Alignment.CenterVertically
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Сегодня",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.align(
                    Alignment.CenterVertically
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Облачно",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.align(
                    Alignment.CenterVertically
                )
            )
        }

        Text(
            text = "17°С/28°С",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.align(
                Alignment.CenterVertically
            )
        )
    }

}

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Image(
            painter = painterResource(id = R.drawable.baseline_dehaze_24),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Bottom)
        )
        Text(text = "Moscow", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Light)
        Image(
            painter = painterResource(id = R.drawable.add),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Bottom)
        )
    }
}

@Composable
fun CityCount(modifier: Modifier) {
    LazyRow(modifier = modifier, content = {
        items(3) {
            CircleBox(Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
        }
    })
}

@Composable
fun CircleBox(modifier: Modifier = Modifier) {
    Box(
        modifier
            .clip(CircleShape)
            .background(Color.White)
    )
}


@Composable
fun TemperatureInfo(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {
        Row {
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = R.drawable.clear_night),
                contentDescription = null,
                Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Clear",
                Modifier.align(Alignment.CenterVertically),
                fontSize = 40.sp,
                color = Color.White,
                fontWeight = FontWeight.Light
            )
        }
        Row {
            Text(
                text = "23",
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
            ) {
                Text(
                    text = "28°C",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.W300,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Box(
                    Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color.White)
                )
                Text(
                    text = "17°C",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.W300,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}


@Preview
@Composable
fun Preview() {
    WeatherScreen()
}