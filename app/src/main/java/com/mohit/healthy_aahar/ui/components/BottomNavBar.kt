package com.mohit.healthy_aahar.ui.components

import android.content.res.Resources.Theme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mohit.healthy_aahar.ui.navigation.Screen
import com.mohit.healthy_aahar.ui.theme.Neutral400
import com.mohit.healthy_aahar.ui.theme.Primary700
import com.mohit.healthy_aahar.ui.theme.Primary800

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun BottomNavBar(navController: NavController, isVisible: Boolean) {
    if (!isVisible) return

    val navItems = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Filled.Home),
        BottomNavItem("Diet Plan", Screen.DietPlan.route, Icons.Filled.Restaurant),
        BottomNavItem("Recipes", Screen.RecipeGenerator.route, Icons.Filled.Fastfood),
        BottomNavItem("Profile", Screen.Profile.route, Icons.Filled.Person)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        navItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary700,
                    selectedTextColor = Primary800,
                    unselectedIconColor = Neutral400,
                    unselectedTextColor = Neutral400,
                    indicatorColor = Color.White
                )
            )
        }
    }
}
