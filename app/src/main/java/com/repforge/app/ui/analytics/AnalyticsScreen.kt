package com.repforge.app.ui.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Analytics", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Weekly Frequency", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text("${state.weeklyWorkouts} workouts this week", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Progress Line (Recent Lifts)", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        if (state.recentProgress.isNotEmpty()) {
            val maxWeight = state.recentProgress.maxOf { it.weightKg }.toFloat().coerceAtLeast(1f)
            val points = state.recentProgress.map { it.weightKg.toFloat() }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
                    .padding(16.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val stepX = width / (points.size.coerceAtLeast(2) - 1).coerceAtLeast(1)
                    
                    val path = Path()
                    points.forEachIndexed { index, weight ->
                        val x = index * stepX
                        val y = height - ((weight / maxWeight) * height * 0.8f) // 0.8 scale to leave top padding
                        if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }
                    
                    drawPath(
                        path = path,
                        color = Color(0xFF4CAF50), // Green Line
                        style = Stroke(width = 6f)
                    )
                }
            }
        } else {
            Text("No recent workout data to graph.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Top Personal Records", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        if (state.personalRecords.isNotEmpty()) {
            LazyColumn {
                items(state.personalRecords) { pr ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(pr.exercise, fontWeight = FontWeight.SemiBold)
                            Text("${pr.maxWeight} kg", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            Text("Complete a workout to see PRs here.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        }
    }
}
