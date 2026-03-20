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

data class ExerciseProgress(
    val dateMillis: Long,
    val weightKg: Double,
    val repsAchieved: String
)

data class AnalyticsState(
    val weeklyWorkouts: Int = 0,
    val personalRecords: List<PR> = emptyList(),
    val allExerciseNames: List<String> = emptyList(),
    val selectedExercise: String = "",
    val exerciseProgressPoints: List<ExerciseProgress> = emptyList(),
    val isBodyweight: Boolean = false,
    val trendUp: Boolean? = null // null = not enough data
)

class AnalyticsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WorkoutRepository(AppDatabase.getDatabase(application).workoutDao())
    private val _state = MutableStateFlow(AnalyticsState())
    val state: StateFlow<AnalyticsState> = _state

    // Bodyweight exercises — no weight tracking
    private val bodyweightExercises = setOf(
        "Pull Ups", "Hanging Leg Raises", "Ab Wheel Rollout",
        "Leg Raises", "Hollow Body Hold", "Plank", "Russian Twists",
        "Decline Sit Ups", "Cable Woodchop", "Face Pulls"
    )

    init {
        loadAnalytics()
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -7)
            val weekAgo = cal.timeInMillis

            val allLogs = repository.getRecentLogs(0)
            val weekLogs = allLogs.filter { it.dateMillis >= weekAgo }

            val uniqueDays = weekLogs.map {
                val c = Calendar.getInstance()
                c.timeInMillis = it.dateMillis
                "${c.get(Calendar.YEAR)}-${c.get(Calendar.DAY_OF_YEAR)}"
            }.distinct().count()

            val prs = allLogs
                .filter { it.weightKg > 0 }
                .groupBy { it.exerciseName }
                .map { (name, logs) -> PR(name, logs.maxOf { it.weightKg }) }
                .sortedByDescending { it.maxWeight }
                .take(5)

            // Get all unique exercise names that have been logged
            val exerciseNames = allLogs.map { it.exerciseName }.distinct().sorted()

            // Default to first weighted exercise
            val defaultExercise = exerciseNames.firstOrNull {
                !bodyweightExercises.contains(it)
            } ?: exerciseNames.firstOrNull() ?: ""

            val currentSelected = if (_state.value.selectedExercise.isNotBlank()
                && exerciseNames.contains(_state.value.selectedExercise)
            ) {
                _state.value.selectedExercise
            } else {
                defaultExercise
            }

            _state.value = _state.value.copy(
                weeklyWorkouts = uniqueDays,
                personalRecords = prs,
                allExerciseNames = exerciseNames
            )

            if (currentSelected.isNotBlank()) {
                selectExercise(currentSelected, allLogs)
            }
        }
    }

    fun selectExercise(name: String) {
        viewModelScope.launch {
            val allLogs = repository.getRecentLogs(0)
            selectExercise(name, allLogs)
        }
    }

    private fun selectExercise(name: String, allLogs: List<ExerciseLog>) {
        val isBW = bodyweightExercises.contains(name)

        val points = allLogs
            .filter { it.exerciseName == name }
            .sortedBy { it.dateMillis }
            .map {
                ExerciseProgress(
                    dateMillis = it.dateMillis,
                    weightKg = it.weightKg,
                    repsAchieved = it.repsAchieved
                )
            }

        // Trend: compare first half avg vs second half avg
        val trendUp: Boolean? = if (points.size >= 3) {
            val half = points.size / 2
            val firstHalfAvg = points.take(half).map {
                if (isBW) it.repsAchieved.split(",")
                    .mapNotNull { r -> r.toIntOrNull() }.average()
                else it.weightKg
            }.average()
            val secondHalfAvg = points.drop(half).map {
                if (isBW) it.repsAchieved.split(",")
                    .mapNotNull { r -> r.toIntOrNull() }.average()
                else it.weightKg
            }.average()
            secondHalfAvg >= firstHalfAvg
        } else null

        _state.value = _state.value.copy(
            selectedExercise = name,
            exerciseProgressPoints = points,
            isBodyweight = isBW,
            trendUp = trendUp
        )
    }
}
