package com.mohit.healthy_aahar.model

data class MealPlanResponse(
    val user_name: String,
    val meal_plan: List<Meal>
)