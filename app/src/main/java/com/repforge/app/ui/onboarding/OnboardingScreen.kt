package com.repforge.app.ui.onboarding

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(modifier: Modifier = Modifier, delayMillis: Long = 800, onTimeout: () -> Unit) {
    val alpha = animateFloatAsState(targetValue = 1f)
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delayMillis)
        onTimeout()
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text("RepForge", style = MaterialTheme.typography.displaySmall, modifier = Modifier.alpha(alpha.value))
    }
}

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pages = listOf(
        "Track your progress with beautiful charts",
        "Start guided workouts with one tap",
        "Personalized plans and insights"
    )
    val pagerState = rememberPagerState(initialPage = 0)
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(48.dp))
        HorizontalPager(pageCount = pages.size, state = pagerState, modifier = Modifier.weight(1f)) { page ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(pages[page], style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(24.dp))
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            repeat(pages.size) { idx ->
                val selected = pagerState.currentPage == idx
                val width = if (selected) 24.dp else 8.dp
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(width, 8.dp)
                        .background(
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { onFinished() }) {
                Text("Skip")
            }
            Button(onClick = {
                if (pagerState.currentPage < pages.size - 1) {
                    scope.launch { pagerState.scrollToPage(pagerState.currentPage + 1) }
                } else {
                    onFinished()
                }
            }) {
                Text(if (pagerState.currentPage < pages.size - 1) "Next" else "Finish")
            }
        }
    }
}
