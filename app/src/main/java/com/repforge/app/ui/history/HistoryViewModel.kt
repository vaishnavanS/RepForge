package com.repforge.app.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.repforge.app.data.AppDatabase
import com.repforge.app.data.entities.ExerciseLog
import com.repforge.app.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class WorkoutSession(
    val dateMillis: Long,
    val title: String,
    val logs: List<ExerciseLog>
) {
    val dateString: String
        get() = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault()).format(Date(dateMillis))
}

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WorkoutRepository(AppDatabase.getDatabase(application).workoutDao())
    
    private val _history = MutableStateFlow<List<WorkoutSession>>(emptyList())
    val history: StateFlow<List<WorkoutSession>> = _history

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            val allLogs = repository.getRecentLogs(0)
            
            // Group by dateMillis (since all logs generated in one finishWorkout call share the same exact timestamp)
            val sessions = allLogs.groupBy { it.dateMillis }
                .map { (date, logs) ->
                    WorkoutSession(
                        dateMillis = date,
                        title = logs.firstOrNull()?.workoutType ?: "Workout",
                        logs = logs
                    )
                }.sortedByDescending { it.dateMillis }
                
            _history.value = sessions
        }
    }
}
