package com.repforge.app.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.repforge.app.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = UserPreferences(application)

    val isLoggedIn: StateFlow<Boolean?> = prefs.isLoggedInFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val userName: StateFlow<String> = prefs.userNameFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Guest")

    val userEmail: StateFlow<String> = prefs.userEmailFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val height: StateFlow<String> = prefs.heightFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val weight: StateFlow<String> = prefs.weightFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val fitnessGoal: StateFlow<String> = prefs.fitnessGoalFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val isDarkMode: StateFlow<Boolean> = prefs.isDarkModeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun login(
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            if (email.isBlank() || pass.isBlank()) {
                onError("Email and password cannot be empty")
                return@launch
            }
            if (pass.length < 6) {
                onError("Password must be at least 6 characters")
                return@launch
            }
            prefs.saveUserSession(
                id = UUID.randomUUID().toString(),
                name = email.substringBefore("@"),
                email = email
            )
            onSuccess()
        }
    }

    fun signup(
        name: String,
        email: String,
        pass: String,
        confirm: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            when {
                name.isBlank() -> onError("Name cannot be empty")
                email.isBlank() -> onError("Email cannot be empty")
                pass.length < 6 -> onError("Password must be at least 6 characters")
                pass != confirm -> onError("Passwords do not match")
                else -> {
                    prefs.saveUserSession(
                        id = UUID.randomUUID().toString(),
                        name = name,
                        email = email
                    )
                    onSuccess()
                }
            }
        }
    }

    fun updateProfile(height: String, weight: String, goal: String) {
        viewModelScope.launch { prefs.updateProfile(height, weight, goal) }
    }

    fun updateName(name: String) {
        viewModelScope.launch { prefs.updateName(name) }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { prefs.setDarkMode(enabled) }
    }

    fun logout() {
        viewModelScope.launch { prefs.clearSession() }
    }
}
