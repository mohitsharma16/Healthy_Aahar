package com.mohit.healthy_aahar.model

data class Meal(
    val _id: String,
    val TranslatedRecipeName: String,
    val Cuisine: String,
    val TotalTimeInMins: Int,
    val TranslatedIngredients: String,
    val TranslatedInstructions: String,
    val CleanedIngredients: String,
    val Calories: Float,
    val Protein: String,
    val Fat: String,
    val Carbs: String
)
