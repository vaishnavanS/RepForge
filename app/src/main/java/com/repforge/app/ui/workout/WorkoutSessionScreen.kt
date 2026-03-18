package com.repforge.app.ui.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WorkoutSessionScreen(
    onFinish: () -> Unit,
    viewModel: WorkoutViewModel = viewModel()
) {
    val currentDay by viewModel.currentDay.collectAsState()
    val exercises by viewModel.exercises.collectAsState()

    if (currentDay == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.finishWorkout(onFinish) },
                icon = { Icon(Icons.Filled.Check, "Finish Workout") },
                text = { Text("Finish Session") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Day ${currentDay!!.dayIndex}: ${currentDay!!.title}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(exercises) { exIndex, exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        onSetChange = { setIdx, wt, reps -> 
                            viewModel.updateSet(exIndex, setIdx, wt, reps) 
                        },
                        onToggleComplete = { setIdx -> 
                            viewModel.toggleSetComplete(exIndex, setIdx) 
                        },
                        onAddSet = {
                            viewModel.addSet(exIndex)
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
                }
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: ExerciseState,
    onSetChange: (Int, String, String) -> Unit,
    onToggleComplete: (Int) -> Unit,
    onAddSet: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                justifyContent = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = exercise.exerciseName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = exercise.prevPerformanceWeight,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            // Header for sets
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Set", modifier = Modifier.weight(0.2f), fontWeight = FontWeight.SemiBold)
                Text("kg/lbs", modifier = Modifier.weight(0.3f), fontWeight = FontWeight.SemiBold)
                Text("Reps", modifier = Modifier.weight(0.3f), fontWeight = FontWeight.SemiBold)
                Text("", modifier = Modifier.weight(0.2f)) // Checkbox
            }
            Spacer(modifier = Modifier.height(8.dp))

            exercise.sets.forEachIndexed { sIndex, set ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${sIndex + 1}", modifier = Modifier.weight(0.2f), fontSize = 16.sp)
                    OutlinedTextField(
                        value = set.weight,
                        onValueChange = { onSetChange(sIndex, it, set.reps) },
                        modifier = Modifier.weight(0.3f).padding(end = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = set.reps,
                        onValueChange = { onSetChange(sIndex, set.weight, it) },
                        modifier = Modifier.weight(0.3f).padding(end = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    androidx.compose.material3.Checkbox(
                        checked = set.isCompleted,
                        onCheckedChange = { onToggleComplete(sIndex) },
                        modifier = Modifier.weight(0.2f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onAddSet, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Icon(Icons.Filled.Add, "Add Set")
                Text("Add Set")
            }
        }
    }
}
