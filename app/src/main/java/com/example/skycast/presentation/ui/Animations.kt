package com.example.skycast.presentation.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.example.skycast.core.SwipeDirection
import kotlinx.coroutines.delay

@Composable
fun <S> AnimateHorizontalSlide(
    state: S,
    modifier: Modifier = Modifier,
    swipeDirection: SwipeDirection,
    content: @Composable() (AnimatedContentScope.(targetState: S) -> Unit)
) {
    AnimatedContent(
        targetState = state, modifier = modifier,
        transitionSpec = {
            if (swipeDirection == SwipeDirection.RIGHT) {
                return@AnimatedContent (slideInHorizontally { height -> height } + fadeIn()).togetherWith(
                    slideOutHorizontally { height -> -height } + fadeOut())
            }
            (slideInHorizontally { height -> -height } + fadeIn()).togetherWith(
                slideOutHorizontally { height -> height } + fadeOut())
        },
        label = "",
        content = content,
    )
}

@Composable
fun AlphaAnimationScope(
    modifier: Modifier = Modifier,
    duration: Int = 2000,
    content: @Composable() (BoxScope.(visible: Boolean) -> Unit)
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(duration.toLong())
            visible = !visible
        }
    }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = duration, easing = FastOutSlowInEasing), label = ""
    )
    Box(modifier = modifier.alpha(alpha)) {
        content(visible)
    }
}