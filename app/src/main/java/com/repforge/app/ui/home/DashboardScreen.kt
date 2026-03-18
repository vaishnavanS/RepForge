package com.repforge.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        while(state.isCardioRunning && state.cardioTimeRemaining > 0) {
            delay(1000L)
            viewModel.tickCardio()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Daily Quote Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = "\"${state.quote}\"",
                modifier = Modifier.padding(16.dp),
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Row
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Card(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Total Sessions", fontSize = 12.sp)
                    Text("${state.totalWorkouts}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
            Card(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Current Streak", fontSize = 12.sp)
                    Text("${state.currentStreak} days", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Next Workout Engine
        Text(
            text = "Next Battle",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Day ${state.nextWorkout.dayIndex}: ${state.nextWorkout.title}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = state.nextWorkout.exercises.joinToString(" • "))
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onStartWorkout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("START SESSION")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Cardio Tracker
        Text(
            text = "Daily 20-Min Cardio",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${state.cardioTimeRemaining / 60}:${String.format("%02d", state.cardioTimeRemaining % 60)}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            
            Button(
                onClick = { viewModel.toggleCardio() },
                enabled = state.cardioTimeRemaining > 0
            ) {
                Text(if (state.cardioTimeRemaining == 0) "DONE" else if (state.isCardioRunning) "PAUSE" else "START")
            }
        }
    }
}
