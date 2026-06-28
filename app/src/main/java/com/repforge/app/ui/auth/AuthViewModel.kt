package com.repforge.app.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.repforge.app.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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

    val joinDateMillis: StateFlow<Long> = prefs.joinDateMillisFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), System.currentTimeMillis())

    val isOnboarded: StateFlow<Boolean> = prefs.isOnboardedFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun login(
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            // Format checks
            when {
                email.isBlank() -> {
                    onError("Please enter your email")
                    return@launch
                }
                !email.contains("@") -> {
                    onError("Please enter a valid email address")
                    return@launch
                }
                pass.isBlank() -> {
                    onError("Please enter your password")
                    return@launch
                }
                pass.length < 6 -> {
                    onError("Password must be at least 6 characters")
                    return@launch
                }
            }

            // Check stored credentials
            val isRegistered = prefs.isRegisteredFlow.first()
            val storedEmail = prefs.userEmailFlow.first()
            val storedPassword = prefs.userPasswordFlow.first()

            when {
                !isRegistered -> {
                    onError("No account found. Please create an account first.")
                    return@launch
                }
                storedEmail.trim().lowercase() != email.trim().lowercase() -> {
                    onError("No account found with this email.")
                    return@launch
                }
                storedPassword != pass -> {
                    onError("Incorrect password. Please try again.")
                    return@launch
                }
                else -> {
                    prefs.setLoggedIn(true)
                    onSuccess()
                }
            }
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
            // Validation
            when {
                name.isBlank() -> {
                    onError("Please enter your full name")
                    return@launch
                }
                email.isBlank() || !email.contains("@") -> {
                    onError("Please enter a valid email address")
                    return@launch
                }
                pass.length < 6 -> {
                    onError("Password must be at least 6 characters")
                    return@launch
                }
                pass != confirm -> {
                    onError("Passwords do not match")
                    return@launch
                }
            }

            // Check if account already exists
            val isRegistered = prefs.isRegisteredFlow.first()
            val storedEmail = prefs.userEmailFlow.first()

            if (isRegistered &&
                storedEmail.trim().lowercase() == email.trim().lowercase()
            ) {
                onError("An account with this email already exists. Please login.")
                return@launch
            }

            // Save account
            prefs.saveUserSession(
                id = UUID.randomUUID().toString(),
                name = name,
                email = email.trim(),
                password = pass
            )
            onSuccess()
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

    fun setOnboarded() {
        viewModelScope.launch { prefs.setOnboarded(true) }
    }
}
