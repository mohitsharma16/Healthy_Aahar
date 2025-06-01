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





data class DailyBreakdown(
    val date: String,
    val day_name: String,
    val totals: NutritionTotals,
    val meals_count: Int,
    val goal_percentage: Double
)

data class GoalAnalysis(
    val target_daily_calories: Double,
    val days_meeting_goal: Int,
    val average_goal_percentage: Double,
    val goal_adherence_rate: Double
)

// New NutritionReport model for custom date ranges
data class NutritionReport(
    val user_id: String,
    val date_range: DateRange,
    val totals: NutritionTotals,
    val averages: NutritionTotals,
    val daily_data: List<DailyData>,
    val summary: ReportSummary
)

data class DateRange(
    val start: String,
    val end: String,
    val total_days: Int,
    val days_with_data: Int
)

data class DailyData(
    val date: String,
    val totals: NutritionTotals,
    val meals_count: Int
)

data class ReportSummary(
    val tracking_percentage: Double,
    val most_active_day: String?
)

// If you don't have these already, add them:

data class RecipeFeedbackItem(
    val _id: String,
    val uid: String,
    val recipe_id: String,
    val rating: Int,
    val comments: String,
    val timestamp: String
)