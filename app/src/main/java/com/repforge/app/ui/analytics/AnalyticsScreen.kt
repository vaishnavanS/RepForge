package com.repforge.app.ui.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    var dropdownExpanded by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("MMM dd", Locale.getDefault()) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = "header") {
            Text(
                "ANALYTICS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                letterSpacing = 3.sp
            )
            Text(
                "Your Progress",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item(key = "weekly") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "WEEKLY WORKOUTS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            "${state.weeklyWorkouts}",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "sessions this week",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                    Text("📊", fontSize = 48.sp)
                }
            }
        }

        item(key = "graph") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(20.dp)
            ) {
                Column {
                    // ── Graph header ────────────────────────
                    val trendUp = state.trendUp
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "PROGRESS TRACKER",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                letterSpacing = 1.sp
                            )
                            Text(
                                if (state.selectedExercise.isNotBlank())
                                    state.selectedExercise
                                else "Select Exercise",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Trend badge
                        if (trendUp != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (trendUp) Color(0xFF4CAF50).copy(alpha = 0.15f)
                                        else Color(0xFFF44336).copy(alpha = 0.15f)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (trendUp)
                                            Icons.Filled.TrendingUp
                                        else
                                            Icons.Filled.TrendingDown,
                                        contentDescription = null,
                                        tint = if (trendUp) Color(0xFF4CAF50)
                                        else Color(0xFFF44336),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        if (trendUp) "Improving" else "Declining",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (trendUp) Color(0xFF4CAF50)
                                        else Color(0xFFF44336)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // ── Exercise Dropdown ────────────────────
                    if (state.allExerciseNames.isNotEmpty()) {
                        ExposedDropdownMenuBox(
                            expanded = dropdownExpanded,
                            onExpandedChange = { dropdownExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = state.selectedExercise.ifBlank { "Select exercise..." },
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        Icons.Filled.ArrowDropDown,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.2f
                                    )
                                ),
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false }
                            ) {
                                state.allExerciseNames.forEach { name ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                name,
                                                fontSize = 14.sp,
                                                fontWeight = if (name == state.selectedExercise)
                                                    FontWeight.Bold else FontWeight.Normal,
                                                color = if (name == state.selectedExercise)
                                                    MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.onSurface
                                            )
                                        },
                                        onClick = {
                                            viewModel.selectExercise(name)
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // ── Chart ────────────────────────────────
                    if (state.exerciseProgressPoints.size >= 2) {
                        val points = state.exerciseProgressPoints
                        val values = if (state.isBodyweight) {
                            points.map { p ->
                                p.repsAchieved.split(",")
                                    .mapNotNull { it.trim().toFloatOrNull() }
                                    .sum()
                            }
                        } else {
                            points.map { it.weightKg.toFloat() }
                        }

                        val maxVal = values.max().coerceAtLeast(1f)
                        val minVal = values.min()
                        val range = (maxVal - minVal).coerceAtLeast(1f)
                        val maxIndex = values.indexOf(values.max())
                        val primaryColor = MaterialTheme.colorScheme.primary

                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Y axis labels
                            Column(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(160.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    if (state.isBodyweight) "${maxVal.toInt()}"
                                    else "${maxVal.toInt()}kg",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                                Text(
                                    if (state.isBodyweight)
                                        "${((maxVal + minVal) / 2).toInt()}"
                                    else "${((maxVal + minVal) / 2).toInt()}kg",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                                Text(
                                    if (state.isBodyweight) "${minVal.toInt()}"
                                    else "${minVal.toInt()}kg",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            Canvas(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(160.dp)
                            ) {
                                val w = size.width
                                val h = size.height
                                val stepX = w / (values.size - 1).coerceAtLeast(1)

                                // Grid lines
                                listOf(0f, 0.33f, 0.66f, 1f).forEach { frac ->
                                    drawLine(
                                        color = Color.Gray.copy(alpha = 0.12f),
                                        start = Offset(0f, h * frac),
                                        end = Offset(w, h * frac),
                                        strokeWidth = 1f
                                    )
                                }

                                // Fill under line
                                val fillPath = Path()
                                values.forEachIndexed { index, value ->
                                    val x = index * stepX
                                    val y = h - ((value - minVal) / range * h * 0.85f) - h * 0.05f
                                    if (index == 0) fillPath.moveTo(x, y)
                                    else fillPath.lineTo(x, y)
                                }
                                fillPath.lineTo((values.size - 1) * stepX, h)
                                fillPath.lineTo(0f, h)
                                fillPath.close()
                                drawPath(
                                    fillPath,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            primaryColor.copy(alpha = 0.25f),
                                            primaryColor.copy(alpha = 0.02f)
                                        ),
                                        startY = 0f,
                                        endY = h
                                    ),
                                    style = Fill
                                )

                                // Line
                                val linePath = Path()
                                values.forEachIndexed { index, value ->
                                    val x = index * stepX
                                    val y = h - ((value - minVal) / range * h * 0.85f) - h * 0.05f
                                    if (index == 0) linePath.moveTo(x, y)
                                    else linePath.lineTo(x, y)
                                }
                                drawPath(linePath, primaryColor, style = Stroke(width = 3f))

                                // Dots — gold for PR
                                values.forEachIndexed { index, value ->
                                    val x = index * stepX
                                    val y = h - ((value - minVal) / range * h * 0.85f) - h * 0.05f
                                    val isPR = index == maxIndex
                                    drawCircle(
                                        color = if (isPR) Color(0xFFFFD700) else primaryColor,
                                        radius = if (isPR) 10f else 6f,
                                        center = Offset(x, y)
                                    )
                                    drawCircle(
                                        color = Color.Black,
                                        radius = if (isPR) 5f else 3f,
                                        center = Offset(x, y)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // X axis dates
                        val showDates = if (points.size <= 6) points
                        else listOf(points.first(), points[points.size / 2], points.last())

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 44.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            showDates.forEach { p ->
                                Text(
                                    dateFormatter.format(Date(p.dateMillis)),
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Stats row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MiniStatBox(
                                modifier = Modifier.weight(1f),
                                label = if (state.isBodyweight) "BEST REPS" else "BEST",
                                value = if (state.isBodyweight)
                                    "${values.max().toInt()} reps"
                                else "${maxVal.toInt()} kg",
                                color = Color(0xFFFFD700)
                            )
                            MiniStatBox(
                                modifier = Modifier.weight(1f),
                                label = "SESSIONS",
                                value = "${points.size}",
                                color = MaterialTheme.colorScheme.primary
                            )
                            MiniStatBox(
                                modifier = Modifier.weight(1f),
                                label = if (state.isBodyweight) "FIRST REPS" else "STARTED AT",
                                value = if (state.isBodyweight)
                                    "${values.first().toInt()} reps"
                                else "${minVal.toInt()} kg",
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                    } else if (state.selectedExercise.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📈", fontSize = 40.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Log at least 2 sessions of\n${state.selectedExercise}\nto see the graph",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🏋️", fontSize = 40.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Complete a workout to see progress",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                            }
                        }
                    }
                }
            }
        }

        item(key = "pr_header") {
            Text(
                "TOP PERSONAL RECORDS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                letterSpacing = 2.sp
            )
        }

        if (state.personalRecords.isNotEmpty()) {
            itemsIndexed(
                items = state.personalRecords,
                key = { _, pr -> pr.exercise }
            ) { index, pr ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        when (index) {
                                            0 -> Color(0xFFFFD700).copy(alpha = 0.2f)
                                            1 -> Color(0xFFC0C0C0).copy(alpha = 0.2f)
                                            2 -> Color(0xFFCD7F32).copy(alpha = 0.2f)
                                            else -> MaterialTheme.colorScheme.primary.copy(
                                                alpha = 0.1f
                                            )
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (index) {
                                        0 -> "🥇"; 1 -> "🥈"; 2 -> "🥉"
                                        else -> "${index + 1}"
                                    },
                                    fontSize = if (index < 3) 18.sp else 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                pr.exercise,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )
                        }
                        Text(
                            "${pr.maxWeight} kg",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        } else {
            item(key = "no_pr") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🏆", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "No records yet. Start lifting!",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }

        item(key = "bottom_space") { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
fun MiniStatBox(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    color: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                label,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
        }
    }
}
