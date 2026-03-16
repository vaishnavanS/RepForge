package com.repforge.app.repository

import com.repforge.app.data.dao.WorkoutDao
import com.repforge.app.data.entities.ExerciseLog

class WorkoutRepository(private val workoutDao: WorkoutDao) {

    /** Insert a new exercise log entry */
    suspend fun insertExerciseLog(log: ExerciseLog) {
        workoutDao.insertExerciseLog(log)
    }

    /**
     * Smart Logging: returns the last recorded performance for a given exercise name.
     * Used to display "Last Workout Performance" above input fields.
     */
    suspend fun getLastPerformance(exerciseName: String): ExerciseLog? {
        return workoutDao.getLastPerformance(exerciseName)
    }

    /**
     * Auto-Progression: If the user hit ALL reps in the last session for this exercise,
     * suggest weight + 2.5 kg. Otherwise suggest the same weight.
     * Returns the suggested weight in kg, or null if there is no history.
     */
    suspend fun getSuggestedWeight(exerciseName: String): Double? {
        val last = workoutDao.getLastPerformance(exerciseName) ?: return null
        return if (last.allRepsHit) {
            last.weightKg + 2.5
        } else {
            last.weightKg
        }
    }

    /**
     * Returns the most recent workout to determine the next day in the 6-day PPL split.
     */
    suspend fun getLastWorkout(): ExerciseLog? {
        return workoutDao.getLastWorkout()
    }

    /**
     * Retrieve all logs since a given timestamp for the Analytics / Chart screen.
     */
    suspend fun getRecentLogs(sinceMillis: Long): List<ExerciseLog> {
        return workoutDao.getRecentLogs(sinceMillis)
    }
}
