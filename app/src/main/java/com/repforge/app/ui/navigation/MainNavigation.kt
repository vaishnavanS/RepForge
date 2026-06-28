package com.repforge.app.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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
import com.repforge.app.ui.onboarding.OnboardingScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector?) {
    object Login : Screen("login", "Login", null)
    object Signup : Screen("signup", "Signup", null)
    object Onboarding : Screen("onboarding", "", null)
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
    val isOnboarded by authViewModel.isOnboarded.collectAsState()

    if (isLoggedIn == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val dashboardViewModel: DashboardViewModel = viewModel()
    val workoutViewModel: WorkoutViewModel = viewModel()
    val analyticsViewModel: AnalyticsViewModel = viewModel()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(Screen.Home, Screen.Workout, Screen.Analytics, Screen.Profile)
    val isBottomNavVisible = bottomNavItems.any { it.route == currentRoute }

    @OptIn(ExperimentalMaterial3Api::class)
    Scaffold(
        topBar = {
            if (currentRoute != Screen.Login.route && currentRoute != Screen.Signup.route && currentRoute != Screen.Onboarding.route) {
                CenterAlignedTopAppBar(
                    title = { Text(bottomNavItems.find { it.route == currentRoute }?.title ?: "RepForge") },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                            Icon(Icons.Filled.Person, contentDescription = "Profile")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            if (currentRoute == Screen.Home.route) {
                androidx.compose.material3.FloatingActionButton(
                    onClick = { navController.navigate(Screen.Workout.route) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.elevation()
                ) {
                    Icon(Icons.Filled.FitnessCenter, contentDescription = "Start")
                }
            }
        },
        bottomBar = {
            if (isBottomNavVisible) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    tonalElevation = 0.dp
                ) {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                val selected = currentRoute == screen.route
                                Icon(screen.icon!!, contentDescription = screen.title, modifier = Modifier.size(if (selected) 28.dp else 22.dp))
                            },
                            label = { Text(screen.title, style = MaterialTheme.typography.labelLarge) },
                            selected = currentRoute == screen.route,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                            ),
                            onClick = {
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
            startDestination = when {
                isLoggedIn == null -> Screen.Login.route
                !isOnboarded -> Screen.Onboarding.route
                isLoggedIn == true -> Screen.Home.route
                else -> Screen.Login.route
            },
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                androidx.compose.animation.slideInHorizontally(initialOffsetX = { 300 }) + androidx.compose.animation.fadeIn()
            },
            exitTransition = {
                androidx.compose.animation.slideOutHorizontally(targetOffsetX = { -300 }) + androidx.compose.animation.fadeOut()
            },
            popEnterTransition = {
                androidx.compose.animation.slideInHorizontally(initialOffsetX = { -300 }) + androidx.compose.animation.fadeIn()
            },
            popExitTransition = {
                androidx.compose.animation.slideOutHorizontally(targetOffsetX = { 300 }) + androidx.compose.animation.fadeOut()
            }
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
            composable(Screen.Onboarding.route) {
                // Onboarding sets the flag and navigates to login
                OnboardingScreen(onFinished = {
                    authViewModel.setOnboarded()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                })
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
                DashboardScreen(
                    onStartWorkout = { navController.navigate(Screen.Workout.route) },
                    viewModel = dashboardViewModel
                )
            }
            composable(Screen.Workout.route) {
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
                AnalyticsScreen(viewModel = analyticsViewModel)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToHistory = { navController.navigate(Screen.History.route) },
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
