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

data class SetState(
    val weight: String = "",
    val reps: String = "",
    val isCompleted: Boolean = false
)

data class ExerciseState(
    val exerciseName: String,
    val sets: List<SetState> = listOf(SetState(), SetState(), SetState()), // 3 sets default
    val prevPerformanceWeight: String = "No History"
)

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val workoutDao = AppDatabase.getDatabase(application).workoutDao()
    private val repository = WorkoutRepository(workoutDao)

    private val _currentDay = MutableStateFlow<WorkoutDay?>(null)
    val currentDay: StateFlow<WorkoutDay?> = _currentDay

    private val _exercises = MutableStateFlow<List<ExerciseState>>(emptyList())
    val exercises: StateFlow<List<ExerciseState>> = _exercises

    init {
        loadNextWorkout()
    }

    private fun loadNextWorkout() {
        viewModelScope.launch {
            val lastWorkoutLog = repository.getLastWorkout()
            val day = PplEngine.getNextWorkout(lastWorkoutLog?.workoutType)
            _currentDay.value = day
            
            val states = day.exercises.map { exName ->
                val prev = repository.getLastPerformance(exName)
                val suggestedW = repository.getSuggestedWeight(exName)
                
                val weightStr = if (suggestedW != null) "Suggested: $suggestedW kg" 
                   else if (prev != null) "Last: ${prev.weightKg} kg" else "No History"
                   
                ExerciseState(exerciseName = exName, prevPerformanceWeight = weightStr)
            }
            _exercises.value = states
        }
    }

    fun updateSet(exerciseIndex: Int, setIndex: Int, weight: String, reps: String) {
        val currentExs = _exercises.value.toMutableList()
        val ex = currentExs[exerciseIndex]
        val newSets = ex.sets.toMutableList()
        newSets[setIndex] = newSets[setIndex].copy(weight = weight, reps = reps)
        currentExs[exerciseIndex] = ex.copy(sets = newSets)
        _exercises.value = currentExs
    }

    fun toggleSetComplete(exerciseIndex: Int, setIndex: Int) {
        val currentExs = _exercises.value.toMutableList()
        val ex = currentExs[exerciseIndex]
        val newSets = ex.sets.toMutableList()
        newSets[setIndex] = newSets[setIndex].copy(isCompleted = !newSets[setIndex].isCompleted)
        currentExs[exerciseIndex] = ex.copy(sets = newSets)
        _exercises.value = currentExs
    }

    fun addSet(exerciseIndex: Int) {
        val currentExs = _exercises.value.toMutableList()
        val ex = currentExs[exerciseIndex]
        val newSets = ex.sets.toMutableList()
        newSets.add(SetState())
        currentExs[exerciseIndex] = ex.copy(sets = newSets)
        _exercises.value = currentExs
    }

    fun finishWorkout(onComplete: () -> Unit) {
        val day = _currentDay.value ?: return
        viewModelScope.launch {
            val dateMillis = System.currentTimeMillis()
            _exercises.value.forEach { ex ->
                val completedSets = ex.sets.filter { it.isCompleted }
                if (completedSets.isNotEmpty()) {
                    val repsAchieved = completedSets.joinToString(",") { it.reps.ifBlank { "0" } }
                    val maxWeight = completedSets.maxOfOrNull { it.weight.toDoubleOrNull() ?: 0.0 } ?: 0.0
                    val allHit = completedSets.all { (it.reps.toIntOrNull() ?: 0) >= 8 } // Assuming 8 is target
                    
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
