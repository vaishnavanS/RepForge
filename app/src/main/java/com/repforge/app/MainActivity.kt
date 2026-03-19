package com.repforge.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.repforge.app.ui.auth.AuthViewModel
import com.repforge.app.ui.navigation.MainAppScreen
import com.repforge.app.ui.theme.RepForgeTheme

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels() // ✅ shared ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ✅ Read dark mode preference and pass it to the theme
            val isDarkMode by authViewModel.isDarkMode.collectAsState()

            RepForgeTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainAppScreen(authViewModel = authViewModel)
                }
            }
        }
    }
}
