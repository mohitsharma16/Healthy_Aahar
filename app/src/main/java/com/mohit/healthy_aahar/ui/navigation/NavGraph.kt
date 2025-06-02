package com.mohit.healthy_aahar.ui.navigation

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
            DietPlanScreen(navController, onMenuClick)
        }
        composable(Screen.RecipeGenerator.route) {
            RecipeGeneratorScreen(navController, onMenuClick)
        }
        composable(Screen.MealHistory.route) {
            MealHistoryScreen(navController)
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
            route = "nutritional_analysis/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
            NutritionalAnalysisScreen(
                navController = navController,
                recipeId = recipeId
            )
        }

        // **Profile & Settings**
        composable(Screen.Profile.route) {
            ProfileScreen( navController ,onSignOut = {
                navController.navigate(Screen.Login.route) {
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