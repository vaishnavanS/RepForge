package com.repforge.app.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.repforge.app.data.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    // ✅ NEW - Dark mode state
    val isDarkMode: StateFlow<Boolean> = prefs.isDarkModeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun login(id: String, name: String, email: String) {
        viewModelScope.launch {
            prefs.saveUserSession(id, name, email)
        }
    }

    fun updateProfile(height: String, weight: String, goal: String) {
        viewModelScope.launch {
            prefs.updateProfile(height, weight, goal)
        }
    }

    fun updateName(name: String) {
        viewModelScope.launch {
            prefs.updateName(name)
        }
    }

    // ✅ NEW - Toggle and save dark mode
    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            prefs.setDarkMode(enabled)
        }
    }

    fun logout() {
        viewModelScope.launch {
            prefs.clearSession()
        }
    }
}
