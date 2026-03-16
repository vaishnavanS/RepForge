package com.repforge.app.domain

data class WorkoutDay(
    val dayIndex: Int,
    val title: String,
    val exercises: List<String>,
    val hasAbs: Boolean = false
)

object PplEngine {
    val sixDaySplit = listOf(
        WorkoutDay(
            dayIndex = 1,
            title = "Push (Heavy) + Abs",
            exercises = listOf(
                "Bench Press", "Incline DB Press", "Shoulder Press", "Lateral Raises", "Tricep Pushdown",
                // Abs circuit
                "Hanging Leg Raises", "Cable Crunch", "Plank"
            ),
            hasAbs = true
        ),
        WorkoutDay(
            dayIndex = 2,
            title = "Pull (Heavy)",
            exercises = listOf(
                "Pull Ups", "Lat Pulldown", "Barbell Row", "Cable Row", "Face Pulls", "Barbell Curls"
            ),
            hasAbs = false
        ),
        WorkoutDay(
            dayIndex = 3,
            title = "Legs (Heavy) + Abs",
            exercises = listOf(
                "Squats", "Romanian Deadlifts", "Leg Press", "Leg Extensions", "Calf Raises",
                // Abs circuit
                "Ab Wheel Rollout", "Leg Raises", "Russian Twists"
            ),
            hasAbs = true
        ),
        WorkoutDay(
            dayIndex = 4,
            title = "Push (Volume)",
            exercises = listOf(
                "DB Bench Press", "Arnold Press", "Machine Chest Press", "Cable Lateral Raises",
                "Skull Crushers", "Weighted Dips"
            ),
            hasAbs = false
        ),
        WorkoutDay(
            dayIndex = 5,
            title = "Pull (Volume)",
            exercises = listOf(
                "Wide Grip Pulldown", "DB Row", "Cable Pullover", "Reverse Pec Deck",
                "Preacher Curls", "Hammer Curls"
            ),
            hasAbs = false
        ),
        WorkoutDay(
            dayIndex = 6,
            title = "Legs (Volume) + Abs",
            exercises = listOf(
                "Front Squats", "Leg Curls", "Bulgarian Split Squats", "Leg Extensions",
                "Donkey Calf Raises",
                // Abs circuit
                "Decline Sit Ups", "Cable Woodchop", "Hollow Body Hold"
            ),
            hasAbs = true
        )
    )

    fun getNextWorkout(lastWorkoutTitle: String?): WorkoutDay {
        if (lastWorkoutTitle == null) return sixDaySplit.first()
        val currentIndex = sixDaySplit.indexOfFirst { it.title == lastWorkoutTitle }
        if (currentIndex == -1) return sixDaySplit.first()
        val nextIndex = (currentIndex + 1) % sixDaySplit.size
        return sixDaySplit[nextIndex]
    }
}
