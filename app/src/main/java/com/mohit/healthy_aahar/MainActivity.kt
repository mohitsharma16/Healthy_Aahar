package com.mohit.healthy_aahar

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import com.mohit.healthy_aahar.repository.AuthRepository
import com.mohit.healthy_aahar.ui.components.AppDrawerContent
import com.mohit.healthy_aahar.ui.components.BottomNavBar
import com.mohit.healthy_aahar.ui.components.RightSideDrawer
import com.mohit.healthy_aahar.ui.navigation.AppNavGraph
import com.mohit.healthy_aahar.ui.navigation.Screen
import com.mohit.healthy_aahar.datastore.UserPreference
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            var isDrawerOpen by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()
            var isNavBarVisible by remember { mutableStateOf(true) }

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            isNavBarVisible = shouldShowBottomNav(currentRoute)

            // Logout function
            val handleLogout = {
                scope.launch {
                    try {
                        // Sign out from Firebase Auth
                        authRepository.logout()


                        // Clear user data from DataStore
                        UserPreference.clearUserData(context)

                        Toast.makeText(context, "Logged out successfully!", Toast.LENGTH_SHORT).show()

                        // Navigate to login screen and clear the entire backstack
                        navController.navigate("login_screen") {
                            popUpTo(0) { inclusive = true }
                        }
                    } catch (e: Exception) {
                        // Handle logout error if needed
                        // You can show a toast or snackbar here
                    }
                }
            }

            Box {
                Scaffold(
                    bottomBar = {
                        if (isNavBarVisible) {
                            BottomNavBar(navController, isNavBarVisible)
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AppNavGraph(
                            navController = navController,
                            authRepository = authRepository,
                            onMenuClick = { isDrawerOpen = true }
                        )
                    }
                }

                RightSideDrawer(
                    isOpen = isDrawerOpen,
                    onClose = { isDrawerOpen = false }
                ) {
                    AppDrawerContent(
                        onNavigate = { route ->
                            isDrawerOpen = false
                            if (route != "logout") { // Don't navigate for logout, it's handled separately
                                navController.navigate(route) {
                                    launchSingleTop = true
                                    popUpTo(Screen.Home.route)
                                }
                            }
                        },
                        onLogout = {
                            isDrawerOpen = false // Close drawer first
                            handleLogout() // Then handle logout
                        }
                    )
                }
            }
        }
    }
}

// **Function to Check If Bottom Nav Should Be Visible**
fun shouldShowBottomNav(route: String?): Boolean {
    return route in listOf(
        Screen.Home.route,
        Screen.DietPlan.route,
        Screen.RecipeGenerator.route,
        Screen.StatisticsScreen.route
    )
}