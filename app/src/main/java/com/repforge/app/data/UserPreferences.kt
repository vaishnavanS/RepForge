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
        val IS_REGISTERED = booleanPreferencesKey("is_registered") // ✅ NEW
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PASSWORD = stringPreferencesKey("user_password") // ✅ NEW
        val HEIGHT = stringPreferencesKey("height")
        val WEIGHT = stringPreferencesKey("weight")
        val FITNESS_GOAL = stringPreferencesKey("fitness_goal")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val JOIN_DATE_MILLIS = longPreferencesKey("join_date_millis")
        val IS_ONBOARDED = booleanPreferencesKey("is_onboarded")
    }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }
    val isRegisteredFlow: Flow<Boolean> = context.dataStore.data.map { it[IS_REGISTERED] ?: false } // ✅ NEW
    val userNameFlow: Flow<String> = context.dataStore.data.map { it[USER_NAME] ?: "Guest" }
    val userEmailFlow: Flow<String> = context.dataStore.data.map { it[USER_EMAIL] ?: "" }
    val userPasswordFlow: Flow<String> = context.dataStore.data.map { it[USER_PASSWORD] ?: "" } // ✅ NEW
    val heightFlow: Flow<String> = context.dataStore.data.map { it[HEIGHT] ?: "" }
    val weightFlow: Flow<String> = context.dataStore.data.map { it[WEIGHT] ?: "" }
    val fitnessGoalFlow: Flow<String> = context.dataStore.data.map { it[FITNESS_GOAL] ?: "" }
    val isDarkModeFlow: Flow<Boolean> = context.dataStore.data.map { it[IS_DARK_MODE] ?: true }
    val joinDateMillisFlow: Flow<Long> = context.dataStore.data.map {
        it[JOIN_DATE_MILLIS] ?: System.currentTimeMillis()
    }
    val isOnboardedFlow: Flow<Boolean> = context.dataStore.data.map { it[IS_ONBOARDED] ?: false }

    // ✅ Updated — now saves password too
    suspend fun saveUserSession(id: String, name: String, email: String, password: String) {
        context.dataStore.edit {
            it[IS_LOGGED_IN] = true
            it[IS_REGISTERED] = true
            it[USER_ID] = id
            it[USER_NAME] = name
            it[USER_EMAIL] = email
            it[USER_PASSWORD] = password
            if (it[JOIN_DATE_MILLIS] == null) {
                it[JOIN_DATE_MILLIS] = System.currentTimeMillis()
            }
        }
    }

    // ✅ NEW — just set logged in without changing registration data
    suspend fun setLoggedIn(value: Boolean) {
        context.dataStore.edit { it[IS_LOGGED_IN] = value }
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

    // ✅ Logout only clears login state — keeps registration data
    suspend fun clearSession() {
        context.dataStore.edit {
            it[IS_LOGGED_IN] = false
            // Keep IS_REGISTERED, USER_EMAIL, USER_PASSWORD so user can log back in
        }
    }

    suspend fun setOnboarded(value: Boolean) {
        context.dataStore.edit { it[IS_ONBOARDED] = value }
    }
}
