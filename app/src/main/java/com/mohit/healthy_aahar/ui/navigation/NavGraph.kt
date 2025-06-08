package com.mohit.healthy_aahar.ui.navigation

import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.auth.oAuthCredential
import com.mohit.healthy_aahar.repository.AuthRepository
import com.mohit.healthy_aahar.ui.screens.auth.*
import com.mohit.healthy_aahar.ui.screens.main.*
import com.mohit.healthy_aahar.ui.screens.profile.*
import com.mohit.healthy_aahar.ui.viewmodel.MainViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authRepository: AuthRepository,
    viewModel: MainViewModel,
    onMenuClick: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
    ) {

        // **Splash Screen - Entry Point**
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigate = { destination ->
                    navController.navigate(destination) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                authRepository = authRepository
            )
        }

        // **Onboarding & Authentication Flow**
        composable(Screen.Onboarding.route) {
            OnboardingScreen( onComplete = {
                val user = authRepository.getCurrentUser()
                if (user != null) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo("onboarding") { inclusive = true }
                    }
                } else {
                    // Navigate directly to Signup instead of Login
                    navController.navigate(Screen.Signup.route) {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            })
        }

        // Removed Login screen composable - only keeping Signup
        composable(Screen.Signup.route) {
            SignupScreen(navController)
        }

        composable(Screen.UserSetup.route) {
            UserSetupScreen {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.UserSetup.route) { inclusive = true }
                }
            }
        }

        // **Main Screens (With Bottom Navigation)**
        composable(Screen.Home.route) {
            HomeScreen(navController, onMenuClick)
        }
        composable(Screen.DietPlan.route) {
            DietPlanScreen(navController, onMenuClick)
        }
        composable(Screen.RecipeGenerator.route) {
            RecipeGeneratorScreen(navController, onMenuClick)
        }
        composable(Screen.MealHistory.route) {
            val currentUser = authRepository.getCurrentUser()
            val uid = currentUser?.uid ?: ""
            MealHistoryScreen(
                navController,
                uid = uid,
                viewModel = viewModel,
            )
        }
        composable(Screen.StatisticsScreen.route) {
            val currentUser = authRepository.getCurrentUser()
            val uid = currentUser?.uid ?: ""
            StatisticsScreen(navController, onMenuClick,viewModel = viewModel,
                uid = uid)
        }
        composable(Screen.FoodLogging.route){
            FoodLoggingScreen(navController)
        }
        composable(
            route = "nutritional_analysis/{mealId}",
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId") ?: ""
            NutritionalAnalysisScreen(
                navController = navController,
                recipeId = mealId // Pass mealId as recipeId to your screen
            )
        }

        // **Profile & Settings**
        composable(Screen.Profile.route) {
            ProfileScreen( navController ,onSignOut = {
                // Navigate to Signup instead of Login on sign out
                navController.navigate(Screen.Signup.route) {
                    popUpTo(Screen.Profile.route) { inclusive = true } // 🔹 Clears profile screen from back stack
                }
            })
        }

        composable(Screen.AboutUs.route){
            AboutUsScreen(navController)
        }

        composable(Screen.Feedback.route){
            val currentUser = authRepository.getCurrentUser()
            val uid = currentUser?.uid ?: ""
            FeedbackScreen(
                navController = navController,
                viewModel = viewModel,
                uid = uid
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
    }
}