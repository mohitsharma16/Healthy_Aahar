package com.mohit.healthy_aahar.model

data class IngredientRecipeRequest(
    val uid: String,
    val ingredients: List<String>
)