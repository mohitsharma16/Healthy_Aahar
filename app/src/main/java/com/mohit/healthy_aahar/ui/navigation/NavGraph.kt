//package com.mohit.healthy_aahar.ui.navigation
//
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import com.mohit.healthy_aahar.ui.screens.auth.*
//import com.mohit.healthy_aahar.ui.screens.main.*
//import com.mohit.healthy_aahar.ui.screens.profile.*
//
//@Composable
//fun AppNavGraph(navController: NavHostController) {
//    NavHost(navController = navController, startDestination = Screen.Onboarding.route) {
//
//        // **Onboarding & Authentication Screens**
//        composable(Screen.Onboarding.route) {
//            OnboardingScreen {
//                navController.navigate(Screen.Login.route) {
//                    popUpTo(Screen.Onboarding.route) { inclusive = true } // Remove Onboarding from back stack
//                }
//            }
//        }
//        composable(Screen.Login.route) { LoginScreen(navController) }
////        composable(Screen.Signup.route) { SignupScreen(navController) }
//        composable(Screen.UserSetup.route) { UserSetupScreen(navController) }
//
//        // **Main Screens (With Bottom Navigation)**
//        composable(Screen.Home.route) { HomeScreen(navController) }
//        composable(Screen.DietPlan.route) { DietPlanScreen(navController) }
//        composable(Screen.RecipeGenerator.route) { RecipeGeneratorScreen(navController) }
//        composable(Screen.FoodLogging.route) { FoodLoggingScreen(navController) }
//        composable(Screen.MealHistory.route) { MealHistoryScreen(navController) }
//        composable(Screen.NutritionalAnalysis.route) { NutritionalAnalysisScreen(navController) }
//
//        // **Profile & Settings**
//        composable(Screen.Profile.route) { ProfileScreen(onSignOut = {
//            navController.navigate(Screen.Login.route) {
//                popUpTo(Screen.Profile.route) { inclusive = true }
//            }
//        }) }
//        composable(Screen.Settings.route) { SettingsScreen(navController) }
//    }
//}

package com.mohit.healthy_aahar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mohit.healthy_aahar.ui.screens.auth.*
import com.mohit.healthy_aahar.ui.screens.main.*
import com.mohit.healthy_aahar.ui.screens.profile.*

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Onboarding.route) {

        // **Onboarding & Authentication Flow**
        composable(Screen.Onboarding.route) {
            OnboardingScreen {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Onboarding.route) { inclusive = true } // 🔹 Clears Onboarding from back stack
                }
            }
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
            HomeScreen(navController)
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
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
    }
}
