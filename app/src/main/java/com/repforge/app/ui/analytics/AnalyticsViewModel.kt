package com.repforge.app.ui.analytics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.repforge.app.data.AppDatabase
import com.repforge.app.data.entities.ExerciseLog
import com.repforge.app.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

data class PR(val exercise: String, val maxWeight: Double)
data class AnalyticsState(
    val weeklyWorkouts: Int = 0,
    val personalRecords: List<PR> = emptyList(),
    val recentProgress: List<ExerciseLog> = emptyList() // For graph
)

class AnalyticsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WorkoutRepository(AppDatabase.getDatabase(application).workoutDao())
    private val _state = MutableStateFlow(AnalyticsState())
    val state: StateFlow<AnalyticsState> = _state

    init {
        loadAnalytics()
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            // week ago timestamp
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -7)
            val weekAgo = cal.timeInMillis

            val recentLogs = repository.getRecentLogs(0) // Get all
            val weekLogs = recentLogs.filter { it.dateMillis >= weekAgo }

            // workout frequency: count unique days in the last 7 days
            val uniqueDays = weekLogs.map { 
                val c = Calendar.getInstance()
                c.timeInMillis = it.dateMillis
                c.get(Calendar.DAY_OF_YEAR)
            }.distinct().count()

            // Calculate PRs (max weight per exercise)
            val prs = recentLogs.groupBy { it.exerciseName }
                .map { (name, logs) -> 
                    PR(name, logs.maxOf { it.weightKg }) 
                }.sortedByDescending { it.maxWeight }.take(5)

            // Progress chart logs: just pick recent logs with a weight to show some trend
            val chartLogs = recentLogs.filter { it.weightKg > 0 }.take(15).reversed()

            _state.value = AnalyticsState(
                weeklyWorkouts = uniqueDays,
                personalRecords = prs,
                recentProgress = chartLogs
            )
        }
    }
}
