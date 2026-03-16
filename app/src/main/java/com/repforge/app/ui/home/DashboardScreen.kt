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
import com.repforge.app.domain.PplEngine
import com.repforge.app.domain.QuotesEngine

@Composable
fun DashboardScreen() {
    val quote = remember { QuotesEngine.getRandomQuote() }
    val nextWorkout = remember { PplEngine.getNextWorkout(null) } // TODO: Fetch real last workout
    
    var cardioTimeRemaining by remember { mutableStateOf(1200) } // 20 mins in seconds
    var isCardioRunning by remember { mutableStateOf(false) }

    // Dummy timer logic (LaunchEffect would handle actual ticking)
    
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
                text = "\"$quote\"",
                modifier = Modifier.padding(16.dp),
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

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
                    text = "Day ${nextWorkout.dayIndex}: ${nextWorkout.title}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = nextWorkout.exercises.joinToString(" • "))
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { /* TODO: Start Workout */ },
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
                text = "${cardioTimeRemaining / 60}:${String.format("%02d", cardioTimeRemaining % 60)}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            
            Button(onClick = { isCardioRunning = !isCardioRunning }) {
                Text(if (isCardioRunning) "PAUSE" else "START")
            }
        }
    }
}
