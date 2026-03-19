package com.repforge.app.ui.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.repforge.app.data.AppDatabase
import com.repforge.app.data.entities.ExerciseLog
import com.repforge.app.domain.PplEngine
import com.repforge.app.domain.WorkoutDay
import com.repforge.app.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

data class SetState(
    val weight: String = "",
    val reps: String = "",
    val isCompleted: Boolean = false
)

data class ExerciseState(
    val exerciseName: String,
    val sets: List<SetState> = listOf(SetState(), SetState(), SetState()),
    val prevPerformanceWeight: String = "No History"
)

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WorkoutRepository(AppDatabase.getDatabase(application).workoutDao())

    private val _currentDay = MutableStateFlow<WorkoutDay?>(null)
    val currentDay: StateFlow<WorkoutDay?> = _currentDay

    private val _exercises = MutableStateFlow<List<ExerciseState>>(emptyList())
    val exercises: StateFlow<List<ExerciseState>> = _exercises

    // ✅ Selected date — defaults to today
    private val _selectedDateMillis = MutableStateFlow(System.currentTimeMillis())
    val selectedDateMillis: StateFlow<Long> = _selectedDateMillis

    // ✅ Selected workout day index (0-5 for 6-day split)
    private val _selectedDayIndex = MutableStateFlow(0)
    val selectedDayIndex: StateFlow<Int> = _selectedDayIndex

    // ✅ All 6 days for the selector
    val allWorkoutDays = PplEngine.sixDaySplit

    init {
        loadNextWorkout()
    }

    private fun loadNextWorkout() {
        viewModelScope.launch {
            val lastWorkoutLog = repository.getLastWorkout()
            val day = PplEngine.getNextWorkout(lastWorkoutLog?.workoutType)
            val dayIndex = PplEngine.sixDaySplit.indexOf(day).coerceAtLeast(0)
            _selectedDayIndex.value = dayIndex
            loadExercisesForDay(day)
        }
    }

    // ✅ Called when user picks a different workout day
    fun selectWorkoutDay(index: Int) {
        _selectedDayIndex.value = index
        val day = PplEngine.sixDaySplit[index]
        _currentDay.value = day
        viewModelScope.launch {
            loadExercisesForDay(day)
        }
    }

    // ✅ Called when user picks a date
    fun selectDate(millis: Long) {
        _selectedDateMillis.value = millis
    }

    private suspend fun loadExercisesForDay(day: WorkoutDay) {
        _currentDay.value = day
        val states = day.exercises.map { exName ->
            val prev = repository.getLastPerformance(exName)
            val suggestedW = repository.getSuggestedWeight(exName)
            val weightStr = when {
                suggestedW != null -> "Suggested: $suggestedW kg"
                prev != null -> "Last: ${prev.weightKg} kg"
                else -> "No History"
            }
            ExerciseState(exerciseName = exName, prevPerformanceWeight = weightStr)
        }
        _exercises.value = states
    }

    fun updateSet(exerciseIndex: Int, setIndex: Int, weight: String, reps: String) {
        val list = _exercises.value.toMutableList()
        val ex = list[exerciseIndex]
        val sets = ex.sets.toMutableList()
        sets[setIndex] = sets[setIndex].copy(weight = weight, reps = reps)
        list[exerciseIndex] = ex.copy(sets = sets)
        _exercises.value = list
    }

    fun toggleSetComplete(exerciseIndex: Int, setIndex: Int) {
        val list = _exercises.value.toMutableList()
        val ex = list[exerciseIndex]
        val sets = ex.sets.toMutableList()
        sets[setIndex] = sets[setIndex].copy(isCompleted = !sets[setIndex].isCompleted)
        list[exerciseIndex] = ex.copy(sets = sets)
        _exercises.value = list
    }

    fun addSet(exerciseIndex: Int) {
        val list = _exercises.value.toMutableList()
        val ex = list[exerciseIndex]
        val sets = ex.sets.toMutableList()
        sets.add(SetState())
        list[exerciseIndex] = ex.copy(sets = sets)
        _exercises.value = list
    }

    fun finishWorkout(onComplete: () -> Unit) {
        val day = _currentDay.value ?: return
        viewModelScope.launch {
            // ✅ Use selected date instead of current time
            val dateMillis = _selectedDateMillis.value
            _exercises.value.forEach { ex ->
                val completedSets = ex.sets.filter { it.isCompleted }
                if (completedSets.isNotEmpty()) {
                    val repsAchieved = completedSets.joinToString(",") { it.reps.ifBlank { "0" } }
                    val maxWeight = completedSets.maxOfOrNull { it.weight.toDoubleOrNull() ?: 0.0 } ?: 0.0
                    val allHit = completedSets.all { (it.reps.toIntOrNull() ?: 0) >= 8 }
                    repository.insertExerciseLog(
                        ExerciseLog(
                            dateMillis = dateMillis,
                            workoutType = day.title,
                            exerciseName = ex.exerciseName,
                            weightKg = maxWeight,
                            repsAchieved = repsAchieved,
                            targetReps = 8,
                            allRepsHit = allHit
                        )
                    )
                }
            }
            onComplete()
        }
    }
}
