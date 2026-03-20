package com.repforge.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.repforge.app.ui.analytics.AnalyticsScreen
import com.repforge.app.ui.analytics.AnalyticsViewModel
import com.repforge.app.ui.auth.AuthViewModel
import com.repforge.app.ui.auth.LoginScreen
import com.repforge.app.ui.auth.SignupScreen
import com.repforge.app.ui.history.HistoryScreen
import com.repforge.app.ui.home.DashboardScreen
import com.repforge.app.ui.home.DashboardViewModel
import com.repforge.app.ui.profile.ProfileScreen
import com.repforge.app.ui.workout.WorkoutSessionScreen
import com.repforge.app.ui.workout.WorkoutViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector?) {
    object Login : Screen("login", "Login", null)
    object Signup : Screen("signup", "Signup", null)
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Workout : Screen("workout", "Workout", Icons.Filled.FitnessCenter)
    object Analytics : Screen("analytics", "Analytics", Icons.Filled.Analytics)
    object Profile : Screen("profile", "Profile", Icons.Filled.Person)
    object History : Screen("history", "History", null)
}

@Composable
fun MainAppScreen(
    authViewModel: AuthViewModel = viewModel()
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    if (isLoggedIn == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // ✅ Hoist all ViewModels here so they are NEVER recreated on tab switch
    val dashboardViewModel: DashboardViewModel = viewModel()
    val workoutViewModel: WorkoutViewModel = viewModel()
    val analyticsViewModel: AnalyticsViewModel = viewModel()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        Screen.Home,
        Screen.Workout,
        Screen.Analytics,
        Screen.Profile
    )

    val isBottomNavVisible = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (isBottomNavVisible) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                Icon(screen.icon!!, contentDescription = screen.title)
                            },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                // ✅ Don't navigate if already on this tab
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn == true) Screen.Home.route
            else Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToSignup = {
                        navController.navigate(Screen.Signup.route)
                    }
                )
            }
            composable(Screen.Signup.route) {
                SignupScreen(
                    onSignupSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Screen.Home.route) {
                // ✅ Pass hoisted ViewModel — never recreated
                DashboardScreen(
                    onStartWorkout = {
                        navController.navigate(Screen.Workout.route)
                    },
                    viewModel = dashboardViewModel
                )
            }
            composable(Screen.Workout.route) {
                // ✅ Pass hoisted ViewModel — never recreated
                WorkoutSessionScreen(
                    onFinish = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                        }
                    },
                    viewModel = workoutViewModel
                )
            }
            composable(Screen.Analytics.route) {
                // ✅ Pass hoisted ViewModel — never recreated
                AnalyticsScreen(viewModel = analyticsViewModel)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToHistory = {
                        navController.navigate(Screen.History.route)
                    },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    viewModel = authViewModel
                )
            }
            composable(Screen.History.route) {
                HistoryScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
