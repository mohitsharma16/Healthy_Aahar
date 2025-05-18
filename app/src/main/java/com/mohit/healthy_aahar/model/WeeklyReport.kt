package com.mohit.healthy_aahar.model

data class WeeklyReport(
    val calories: List<Double>,
    val protein: List<Double>,
    val carbs: List<Double>,
    val fats: List<Double>,
    val dates: List<String>
)
