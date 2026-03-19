package com.repforge.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {
    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val HEIGHT = stringPreferencesKey("height")
        val WEIGHT = stringPreferencesKey("weight")
        val FITNESS_GOAL = stringPreferencesKey("fitness_goal")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val JOIN_DATE_MILLIS = longPreferencesKey("join_date_millis") // ✅ NEW
    }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }
    val userNameFlow: Flow<String> = context.dataStore.data.map { it[USER_NAME] ?: "Guest" }
    val userEmailFlow: Flow<String> = context.dataStore.data.map { it[USER_EMAIL] ?: "" }
    val heightFlow: Flow<String> = context.dataStore.data.map { it[HEIGHT] ?: "" }
    val weightFlow: Flow<String> = context.dataStore.data.map { it[WEIGHT] ?: "" }
    val fitnessGoalFlow: Flow<String> = context.dataStore.data.map { it[FITNESS_GOAL] ?: "" }
    val isDarkModeFlow: Flow<Boolean> = context.dataStore.data.map { it[IS_DARK_MODE] ?: true }
    val joinDateMillisFlow: Flow<Long> = context.dataStore.data.map { // ✅ NEW
        it[JOIN_DATE_MILLIS] ?: System.currentTimeMillis()
    }

    suspend fun saveUserSession(id: String, name: String, email: String) {
        context.dataStore.edit {
            it[IS_LOGGED_IN] = true
            it[USER_ID] = id
            it[USER_NAME] = name
            it[USER_EMAIL] = email
            // ✅ Only set join date if not already set
            if (it[JOIN_DATE_MILLIS] == null) {
                it[JOIN_DATE_MILLIS] = System.currentTimeMillis()
            }
        }
    }

    suspend fun updateProfile(height: String, weight: String, goal: String) {
        context.dataStore.edit {
            it[HEIGHT] = height
            it[WEIGHT] = weight
            it[FITNESS_GOAL] = goal
        }
    }

    suspend fun updateName(name: String) {
        context.dataStore.edit { it[USER_NAME] = name }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[IS_DARK_MODE] = enabled }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}
