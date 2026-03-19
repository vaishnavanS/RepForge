package com.repforge.app.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.repforge.app.ui.auth.AuthViewModel

@Composable
fun ProfileScreen(
    onNavigateToHistory: () -> Unit,
    onLogout: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    // ✅ Read dark mode from ViewModel (persisted), not local remember
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val height by viewModel.height.collectAsState()
    val weight by viewModel.weight.collectAsState()
    val fitnessGoal by viewModel.fitnessGoal.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var editHeight by remember(height) { mutableStateOf(height) }
    var editWeight by remember(weight) { mutableStateOf(weight) }
    var editGoal by remember(fitnessGoal) { mutableStateOf(fitnessGoal) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Surface(
            modifier = Modifier.size(120.dp),
            shape = androidx.compose.foundation.shape.CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(userName.take(2).uppercase(), fontSize = 40.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(userName, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Joined: Oct 2023", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

        Spacer(modifier = Modifier.height(24.dp))

        if (isEditing) {
            OutlinedTextField(
                value = editHeight,
                onValueChange = { editHeight = it },
                label = { Text("Height (cm/in)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = editWeight,
                onValueChange = { editWeight = it },
                label = { Text("Weight (kg/lbs)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = editGoal,
                onValueChange = { editGoal = it },
                label = { Text("Fitness Goal") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.updateProfile(editHeight, editWeight, editGoal)
                    isEditing = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Profile")
            }
        } else {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Height: ${if (height.isNotBlank()) height else "Not set"}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Weight: ${if (weight.isNotBlank()) weight else "Not set"}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Goal: ${if (fitnessGoal.isNotBlank()) fitnessGoal else "Not set"}")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { isEditing = true }) {
                Text("Edit Profile Details")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Theme Preference", fontSize = 18.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(if (isDarkMode) "Dark (Neon)" else "Light (Navy)")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = isDarkMode,
                        // ✅ Now actually saves to DataStore and triggers theme change
                        onCheckedChange = { viewModel.setDarkMode(it) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNavigateToHistory,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("VIEW WORKOUT HISTORY")
        }

        OutlinedButton(
            onClick = {
                viewModel.logout()
                onLogout()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("LOGOUT", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
