package com.repforge.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.repforge.app.data.entities.ExerciseLog

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insertExerciseLog(log: ExerciseLog)

    @Query("SELECT * FROM exercise_logs WHERE exerciseName = :name ORDER BY dateMillis DESC LIMIT 1")
    suspend fun getLastPerformance(name: String): ExerciseLog?

    @Query("SELECT * FROM exercise_logs WHERE dateMillis >= :sinceMillis ORDER BY dateMillis DESC")
    suspend fun getRecentLogs(sinceMillis: Long): List<ExerciseLog>

    // Retrieve the most recent workout to determine the next PPL day
    @Query("SELECT * FROM exercise_logs ORDER BY dateMillis DESC LIMIT 1")
    suspend fun getLastWorkout(): ExerciseLog?
}
