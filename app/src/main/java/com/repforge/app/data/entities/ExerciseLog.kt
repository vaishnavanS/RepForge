package com.repforge.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_logs")
data class ExerciseLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateMillis: Long,
    val workoutType: String, // e.g., "Day 1: Push (Heavy) + Abs"
    val exerciseName: String, // e.g., "Bench Press"
    val weightKg: Double, // Weight lifted
    val repsAchieved: String, // e.g., "8,8,8" (comma-separated for sets)
    val targetReps: Int, // The goal reps per set
    val allRepsHit: Boolean // Identifies if user hit all reps for auto progression (+2.5kg)
)
