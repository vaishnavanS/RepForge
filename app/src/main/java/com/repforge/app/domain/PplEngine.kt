package com.repforge.app.domain

enum class WorkoutRoutine(val displayName: String) {
    PUSH_PULL_LEGS("PPL"),
    BRO_SPLIT("Bro Split")
}

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
                "Hanging Leg Raises", "Cable Crunch", "Plank"
            ),
            hasAbs = true
        ),
        WorkoutDay(
            dayIndex = 2,
            title = "Pull (Heavy)",
            exercises = listOf(
                "Pull Ups", "Lat Pulldown", "Barbell Row", "Cable Row", "Face Pulls", "Barbell Curls"
            )
        ),
        WorkoutDay(
            dayIndex = 3,
            title = "Legs (Heavy) + Abs",
            exercises = listOf(
                "Squats", "Romanian Deadlifts", "Leg Press", "Leg Extensions", "Calf Raises",
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
            )
        ),
        WorkoutDay(
            dayIndex = 5,
            title = "Pull (Volume)",
            exercises = listOf(
                "Wide Grip Pulldown", "DB Row", "Cable Pullover", "Reverse Pec Deck",
                "Preacher Curls", "Hammer Curls"
            )
        ),
        WorkoutDay(
            dayIndex = 6,
            title = "Legs (Volume) + Abs",
            exercises = listOf(
                "Front Squats", "Leg Curls", "Bulgarian Split Squats", "Leg Extensions",
                "Donkey Calf Raises", "Decline Sit Ups", "Cable Woodchop", "Hollow Body Hold"
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

object BroEngine {
    val fiveDaySplit = listOf(
        WorkoutDay(
            dayIndex = 1,
            title = "Chest & Triceps",
            exercises = listOf(
                "Bench Press", "Incline DB Press", "Cable Fly", "Dips", "Skull Crushers"
            )
        ),
        WorkoutDay(
            dayIndex = 2,
            title = "Back & Biceps",
            exercises = listOf(
                "Deadlift", "Pull Ups", "Barbell Row", "Cable Row", "Hammer Curls", "Face Pulls"
            )
        ),
        WorkoutDay(
            dayIndex = 3,
            title = "Legs",
            exercises = listOf(
                "Squats", "Leg Press", "Romanian Deadlifts", "Leg Extensions", "Calf Raises"
            )
        ),
        WorkoutDay(
            dayIndex = 4,
            title = "Shoulders & Abs",
            exercises = listOf(
                "Shoulder Press", "Lateral Raises", "Rear Delt Fly", "Cable Face Pull", "Hanging Leg Raises", "Plank"
            ),
            hasAbs = true
        ),
        WorkoutDay(
            dayIndex = 5,
            title = "Arms & Core",
            exercises = listOf(
                "Barbell Curls", "Tricep Dips", "Incline DB Curl", "Overhead Tricep Extension", "Cable Woodchop", "Russian Twists"
            ),
            hasAbs = true
        )
    )

    fun getNextWorkout(lastWorkoutTitle: String?): WorkoutDay {
        if (lastWorkoutTitle == null) return fiveDaySplit.first()
        val currentIndex = fiveDaySplit.indexOfFirst { it.title == lastWorkoutTitle }
        if (currentIndex == -1) return fiveDaySplit.first()
        val nextIndex = (currentIndex + 1) % fiveDaySplit.size
        return fiveDaySplit[nextIndex]
    }
}

fun getRoutineDays(routine: WorkoutRoutine): List<WorkoutDay> = when (routine) {
    WorkoutRoutine.PUSH_PULL_LEGS -> PplEngine.sixDaySplit
    WorkoutRoutine.BRO_SPLIT -> BroEngine.fiveDaySplit
}

fun getNextWorkout(lastWorkoutTitle: String?, routine: WorkoutRoutine): WorkoutDay = when (routine) {
    WorkoutRoutine.PUSH_PULL_LEGS -> PplEngine.getNextWorkout(lastWorkoutTitle)
    WorkoutRoutine.BRO_SPLIT -> BroEngine.getNextWorkout(lastWorkoutTitle)
}
