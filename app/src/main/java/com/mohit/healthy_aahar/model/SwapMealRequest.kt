package com.mohit.healthy_aahar.model
//
////data class UserProfile(
////    val name: String,
////    val age: Int,
////    val gender: String,
////    val weight: Float,
////    val height: Float,
////    val activity_level: String,
////    val goal: String
////)
////
////data class MealPlanResponse(
////    val user_name: String,
////    val meal_plan: List<Meal>
////)
////
////data class Meal(
////    val TranslatedRecipeName: String,
////    val Cuisine: String,
////    val TotalTimeInMins: Int,
////    val TranslatedInstructions: String,
////    val Calories: Int,
////    val Protein: Float,
////    val Fat: Float,
////    val Carbs: Float
////)
////
////data class RecipeRequest(
////    val ingredients: String,
////    val num_recommendations: Int = 5
////)
//
//// User Profile Model
//data class UserProfile(
//    val name: String,
//    val age: Int,
//    val gender: String,
//    val weight: Float,
//    val height: Float,
//    val activity_level: String,
//    val goal: String
//)
//
//// Recipe Model (renamed for consistency)
//data class Recipe(
//    val TranslatedRecipeName: String,
//    val Cuisine: String,
//    val TotalTimeInMins: Int,
//    val TranslatedInstructions: String,
//    val Calories: Int,
//    val Protein: Float,
//    val Fat: Float,
//    val Carbs: Float
//)
//
//// Meal Plan Response
//data class MealPlanResponse(
//    val user_name: String,
//    val meal_plan: List<Recipe>
//)
//
//// Swap Meal Request
//data class SwapMealRequest(
//    val meal_index: Int
//)
//
//// Swap Meal Response
//data class SwapMealResponse(
//    val message: String,
//    val new_meal: Recipe
//)

data class SwapMealRequest(
    val meal_index: Int
)