package com.mohit.healthy_aahar.model

data class DailyNutrition(
    val uid: String,
    val date: String,
    val meals: List<LoggedMeal>,
    val total: NutritionTotals
)

data class LoggedMeal(
    val meal_id: String,
    val meal_name: String,
    val calories: Int,
    val protein: Int,
    val fat: Int,
    val carbs: Int
)

data class NutritionTotals(
    val calories: Int,
    val protein: Int,
    val fat: Int,
    val carbs: Int
)