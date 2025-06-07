package com.mohit.healthy_aahar.ui.navigation

sealed class Screen(val route: String) {
    // **Onboarding & Authentication Screens**
    object Splash: Screen("splash_screen")
    object Onboarding : Screen("onboarding_screen")
    object Login : Screen("login_screen")
    object Signup : Screen("signup_screen")
    object UserSetup : Screen("user_setup_screen")

    // **Main App Screens**
    object Home : Screen("home_screen")
    object DietPlan : Screen("diet_plan_screen")
    object RecipeGenerator : Screen("recipe_generator_screen")
    object FoodLogging : Screen("food_logging_screen")
    object MealHistory : Screen("meal_history_screen")
    object StatisticsScreen : Screen("statistics_screen")
//    object NutritionalAnalysis : Screen("nutritional_analysis_screen")

    // **Profile & Settings Screens**
    object Profile : Screen("profile_screen")
    object Settings : Screen("settings_screen")

    object AboutUs : Screen( "aboutus_screen")
    object Feedback : Screen( "feedback_screen")

    object NutritionalAnalysis : Screen("nutritional_analysis") {
        const val routeWithArgs = "nutritional_analysis/{recipeId}"
        fun createRoute(recipeId: String) = "nutritional_analysis/$recipeId"
    }
}
