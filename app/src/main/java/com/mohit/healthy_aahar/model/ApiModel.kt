package com.mohit.healthy_aahar.model

data class UserProfile(
    val name: String,
    val age: Int,
    val gender: String,
    val weight: Float,
    val height: Float,
    val activity_level: String,
    val goal: String
)

data class MealPlanResponse(
    val user_name: String,
    val meal_plan: List<Meal>
)

data class Meal(
    val TranslatedRecipeName: String,
    val Cuisine: String,
    val TotalTimeInMins: Int,
    val TranslatedInstructions: String,
    val Calories: Int,
    val Protein: Float,
    val Fat: Float,
    val Carbs: Float
)

data class RecipeRequest(
    val ingredients: String,
    val num_recommendations: Int = 5
)
