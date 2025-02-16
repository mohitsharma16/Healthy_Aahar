package com.mohit.healthy_aahar.model

data class HealthReport(
    val id: Int = 0,  // 🔥 PRIMARY KEY IS REQUIRED
    val userId: String,
    val bmi: Float,
    val caloriesIntake: Int,
    val date: Long
)
