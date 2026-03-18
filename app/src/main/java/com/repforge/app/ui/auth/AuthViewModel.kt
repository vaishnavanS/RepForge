package com.repforge.app.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.repforge.app.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application)

    val isLoggedIn: StateFlow<Boolean?> = userPreferences.isLoggedInFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
        
    val userName: StateFlow<String> = userPreferences.userNameFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, "Guest")
        
    val height: StateFlow<String> = userPreferences.heightFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    val weight: StateFlow<String> = userPreferences.weightFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    val fitnessGoal: StateFlow<String> = userPreferences.fitnessGoalFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            onError("Email and password cannot be empty")
            return
        }
        viewModelScope.launch {
            val name = email.substringBefore("@").replaceFirstChar { it.uppercase() }
            userPreferences.saveUserSession(id = "user_123", name = name, email = email)
            onSuccess()
        }
    }

    fun signup(name: String, email: String, pass: String, confirm: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            onError("All fields are required")
            return
        }
        if (pass != confirm) {
            onError("Passwords do not match")
            return
        }
        if (pass.length < 6) {
            onError("Password must be at least 6 characters")
            return
        }
        viewModelScope.launch {
            userPreferences.saveUserSession(id = "user_${System.currentTimeMillis()}", name = name, email = email)
            onSuccess()
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            userPreferences.clearSession()
        }
    }

    fun updateProfile(newHeight: String, newWeight: String, newGoal: String) {
        viewModelScope.launch {
            userPreferences.updateProfile(height = newHeight, weight = newWeight, goal = newGoal)
        }
    }
    
    fun updateName(newName: String) {
        viewModelScope.launch {
            userPreferences.updateName(newName)
        }
    }
}
