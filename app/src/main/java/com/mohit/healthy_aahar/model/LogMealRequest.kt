package com.mohit.healthy_aahar.model

data class LogMealRequest(
    val uid: String,
    val meal_id: String,
    val date: String  // Format: "YYYY-MM-DD"
)