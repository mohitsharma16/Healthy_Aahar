package com.mohit.healthy_aahar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.oAuthCredential
import com.mohit.healthy_aahar.repository.AuthRepository
import com.mohit.healthy_aahar.ui.screens.auth.*
import com.mohit.healthy_aahar.ui.screens.main.*
import com.mohit.healthy_aahar.ui.screens.profile.*

@Composable
fun AppNavGraph(navController: NavHostController, authRepository: AuthRepository, onMenuClick: () -> Unit) {
    NavHost(navController = navController,
        startDestination = Screen.Onboarding.route,
        ) {

        // **Onboarding & Authentication Flow**
        composable(Screen.Onboarding.route) {
            OnboardingScreen( onComplete = {
                val user = authRepository.getCurrentUser()
                if (user != null) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo("onboarding") { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.Login.route) {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            })
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
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
            DietPlanScreen(navController)
        }
        composable(Screen.RecipeGenerator.route) {
            RecipeGeneratorScreen(navController)
        }
        composable(Screen.MealHistory.route) {
            MealHistoryScreen(navController)
        }
        composable(Screen.StatisticsScreen.route) {
            StatisticsScreen(navController)
        }
        composable(Screen.FoodLogging.route){
            FoodLoggingScreen(navController)
        }
        composable(Screen.NutritionalAnalysis.route) {
            NutritionalAnalysisScreen(navController)
        }


        // **Profile & Settings**
        composable(Screen.Profile.route) {
            ProfileScreen(onSignOut = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Profile.route) { inclusive = true } // 🔹 Clears profile screen from back stack
                }
            })
        }

        composable(Screen.AboutUs.route){
            AboutUsScreen()
        }

        composable(Screen.Feedback.route){
            FeedbackScreen(navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
    }
}
