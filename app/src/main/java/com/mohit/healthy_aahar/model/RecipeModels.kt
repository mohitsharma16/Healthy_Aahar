package com.mohit.healthy_aahar.model

// Model for getting recipe details
data class RecipeDetails(
    val _id: String,
    val TranslatedRecipeName: String,
    val Cuisine: String,
    val TotalTimeInMins: Int,
    val TranslatedInstructions: String,
    val Calories: Int,
    val Protein: Int,
    val Fat: Int,
    val Carbs: Int,
    val average_rating: Double = 0.0,
    val feedback_count: Int = 0
)

// Model for recipe feedback
data class RecipeFeedback(
    val _id: String,
    val uid: String,
    val recipe_id: String,
    val rating: Int,
    val comments: String,
    val timestamp: String
)

// Model for recipe with feedback
data class RecipeWithFeedback(
    val _id: String,
    val TranslatedRecipeName: String,
    val Cuisine: String,
    val TotalTimeInMins: Int,
    val TranslatedInstructions: String,
    val Calories: Int,
    val Protein: Int,
    val Fat: Int,
    val Carbs: Int,
    val average_rating: Double,
    val feedback_count: Int,
    val feedback: List<RecipeFeedback>
)

// Model for recipe search response
data class RecipeSearchResponse(
    val message: String,
    val recipes: List<RecipeDetails>
)

// Fix for getMealHistory endpoint - updated to match backend
// Note: Your existing getMealHistory() method should be updated to include query parameters