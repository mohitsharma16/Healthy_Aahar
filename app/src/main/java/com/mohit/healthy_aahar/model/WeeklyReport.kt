package com.mohit.healthy_aahar.model

data class WeeklyReport(
    val user_id: String,
    val user_name: String,
    val week_start: String,
    val week_end: String,
    val days_logged: Int,
    val total_days: Int,
    val weekly_totals: NutritionTotals,
    val daily_averages: NutritionTotals,
    val daily_breakdown: List<DailyBreakdown>,
    val goal_analysis: GoalAnalysis,
    val insights: List<String>
)