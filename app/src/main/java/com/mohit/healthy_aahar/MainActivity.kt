package com.mohit.healthy_aahar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mohit.healthy_aahar.repository.AuthRepository
import com.mohit.healthy_aahar.ui.components.AppDrawerContent
import com.mohit.healthy_aahar.ui.components.BottomNavBar
import com.mohit.healthy_aahar.ui.components.RightSideDrawer
import com.mohit.healthy_aahar.ui.navigation.AppNavGraph
import com.mohit.healthy_aahar.ui.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var isDrawerOpen by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()
            var isNavBarVisible by remember { mutableStateOf(true) }

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            isNavBarVisible = shouldShowBottomNav(currentRoute)

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
                    AppDrawerContent { route ->
                        isDrawerOpen = false
                        navController.navigate(route) {
                            launchSingleTop = true
                            popUpTo(Screen.Home.route)
                        }
                    }
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
