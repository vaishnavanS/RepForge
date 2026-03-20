package com.repforge.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen(
    onStartWorkout: () -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isCardioRunning) {
        while (state.isCardioRunning && state.cardioTimeRemaining > 0) {
            delay(1000L)
            viewModel.tickCardio()
        }
    }

    // ✅ LazyColumn instead of Column+verticalScroll — much smoother
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {

        // ── Header ──────────────────────────────────────
        item(key = "header") {
            Column {
                Text(
                    text = "REPFORGE",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 4.sp
                )
                Text(
                    text = "Intelligent Strength Assistant",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    letterSpacing = 1.sp
                )
            }
        }

        // ── Streak Motivation Card ───────────────────────
        item(key = "motivation") {
            val motivationMessage = when {
                state.currentStreak == 0 -> Triple(
                    "🚀", "Start Your Journey",
                    "Every champion was once a beginner. Today is day one."
                )
                state.currentStreak == 1 -> Triple(
                    "⚡", "You Showed Up!",
                    "The hardest part is starting. You did it. Keep going."
                )
                state.currentStreak in 2..4 -> Triple(
                    "🔥", "${state.currentStreak} Day Streak!",
                    "You're building momentum. Don't break the chain!"
                )
                state.currentStreak in 5..9 -> Triple(
                    "💪", "${state.currentStreak} Days Strong!",
                    "Consistency is your superpower. You're proving it daily."
                )
                state.currentStreak in 10..20 -> Triple(
                    "🏆", "${state.currentStreak} Day Warrior!",
                    "Most people quit by now. You're not most people."
                )
                else -> Triple(
                    "👑", "${state.currentStreak} Day Legend!",
                    "Absolute elite. Your discipline is your identity now."
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(motivationMessage.first, fontSize = 44.sp)
                    Column {
                        Text(
                            motivationMessage.second,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            motivationMessage.third,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // ── Stats Row ────────────────────────────────────
        item(key = "stats") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "Total Sessions",
                    value = "${state.totalWorkouts}",
                    icon = "🏋️"
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "Streak",
                    value = "${state.currentStreak} days",
                    icon = "🔥"
                )
            }
        }

        // ── Next Session ─────────────────────────────────
        item(key = "next_session_label") {
            Text(
                text = "NEXT SESSION",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                letterSpacing = 2.sp
            )
        }

        item(key = "next_session_card") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "Day ${state.nextWorkout.dayIndex}",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = state.nextWorkout.title,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("💪", fontSize = 24.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Exercise chips
                    val exercises = state.nextWorkout.exercises
                    val preview = exercises.take(4)
                    val remaining = exercises.size - preview.size

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        preview.forEach { ex ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = ex.split(" ").first(),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        if (remaining > 0) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "+$remaining more",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onStartWorkout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            "START SESSION",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }
        }

        // ── Cardio Timer Label ───────────────────────────
        item(key = "cardio_label") {
            Text(
                text = "CARDIO TIMER",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                letterSpacing = 2.sp
            )
        }

        // ── Cardio Timer Card ────────────────────────────
        item(key = "cardio_card") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "20-Min Cardio",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "${state.cardioTimeRemaining / 60}:${
                                String.format("%02d", state.cardioTimeRemaining % 60)
                            }",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Black,
                            color = if (state.cardioTimeRemaining == 0)
                                MaterialTheme.colorScheme.secondary
                            else
                                MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { 1f - (state.cardioTimeRemaining / 1200f) },
                            modifier = Modifier
                                .width(180.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (state.cardioTimeRemaining == 0)
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                                else
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { viewModel.toggleCardio() },
                            enabled = state.cardioTimeRemaining > 0
                        ) {
                            Icon(
                                imageVector = if (state.isCardioRunning) Icons.Filled.Stop
                                else Icons.Filled.PlayArrow,
                                contentDescription = "Toggle Cardio",
                                tint = if (state.cardioTimeRemaining == 0)
                                    MaterialTheme.colorScheme.secondary
                                else
                                    MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Column {
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}
