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
    }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    val userNameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME] ?: "Guest"
    }

    val userEmailFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL] ?: ""
    }

    val heightFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[HEIGHT] ?: ""
    }

    val weightFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[WEIGHT] ?: ""
    }

    val fitnessGoalFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[FITNESS_GOAL] ?: ""
    }

    suspend fun saveUserSession(id: String, name: String, email: String) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = id
            preferences[USER_NAME] = name
            preferences[USER_EMAIL] = email
        }
    }

    suspend fun updateProfile(height: String, weight: String, goal: String) {
        context.dataStore.edit { preferences ->
            preferences[HEIGHT] = height
            preferences[WEIGHT] = weight
            preferences[FITNESS_GOAL] = goal
        }
    }

    suspend fun updateName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
