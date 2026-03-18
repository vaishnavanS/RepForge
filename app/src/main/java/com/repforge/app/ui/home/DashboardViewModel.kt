package com.repforge.app.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.repforge.app.data.AppDatabase
import com.repforge.app.domain.PplEngine
import com.repforge.app.domain.QuotesEngine
import com.repforge.app.domain.WorkoutDay
import com.repforge.app.repository.WorkoutRepository
import com.repforge.app.data.entities.ExerciseLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

data class DashboardState(
    val quote: String = QuotesEngine.getRandomQuote(),
    val nextWorkout: WorkoutDay = PplEngine.sixDaySplit.first(),
    val lastWorkoutTitle: String = "None yet",
    val totalWorkouts: Int = 0,
    val currentStreak: Int = 0,
    val cardioTimeRemaining: Int = 1200, // 20 mins
    val isCardioRunning: Boolean = false
)

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WorkoutRepository(AppDatabase.getDatabase(application).workoutDao())
    
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            val allLogs = repository.getRecentLogs(0)
            val total = allLogs.distinctBy { 
                val c = Calendar.getInstance()
                c.timeInMillis = it.dateMillis
                c.get(Calendar.DAY_OF_YEAR)
            }.size
            
            val lastWorkout = repository.getLastWorkout()
            val lastTitle = lastWorkout?.workoutType ?: "None yet"
            val next = PplEngine.getNextWorkout(lastWorkout?.workoutType)
            
            // Calculate streak (very basic active days streak)
            var streak = 0
            if (allLogs.isNotEmpty()) {
                val uniqueDays = allLogs.map { 
                    val c = Calendar.getInstance()
                    c.timeInMillis = it.dateMillis
                    c.get(Calendar.DAY_OF_YEAR)
                }.distinct().sortedDescending()
                
                val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                if (uniqueDays.isNotEmpty() && (uniqueDays[0] == today || uniqueDays[0] == today - 1)) {
                    streak = 1
                    var prevDay = uniqueDays[0]
                    for (i in 1 until uniqueDays.size) {
                        if (uniqueDays[i] == prevDay - 1) {
                            streak++
                            prevDay = uniqueDays[i]
                        } else {
                            break
                        }
                    }
                }
            }

            _state.value = _state.value.copy(
                quote = QuotesEngine.getRandomQuote(),
                nextWorkout = next,
                lastWorkoutTitle = lastTitle,
                totalWorkouts = total,
                currentStreak = streak
            )
        }
    }

    fun toggleCardio() {
        _state.value = _state.value.copy(isCardioRunning = !_state.value.isCardioRunning)
    }

    fun tickCardio() {
        val current = _state.value
        if (current.isCardioRunning && current.cardioTimeRemaining > 0) {
            val newTime = current.cardioTimeRemaining - 1
            if (newTime == 0) {
                _state.value = current.copy(cardioTimeRemaining = 0, isCardioRunning = false)
                saveCardio()
            } else {
                _state.value = current.copy(cardioTimeRemaining = newTime)
            }
        }
    }

    private fun saveCardio() {
        viewModelScope.launch {
            repository.insertExerciseLog(
                ExerciseLog(
                    dateMillis = System.currentTimeMillis(),
                    workoutType = "Cardio",
                    exerciseName = "20-Min Cardio",
                    weightKg = 0.0,
                    repsAchieved = "1",
                    targetReps = 1,
                    allRepsHit = true
                )
            )
            loadDashboard() // refresh streaks
        }
    }
}
