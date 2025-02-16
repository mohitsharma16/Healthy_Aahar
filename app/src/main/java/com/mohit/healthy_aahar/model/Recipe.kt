package com.mohit.healthy_aahar.model

data class Recipe(
    val id: String,  // 🔥 PRIMARY KEY IS REQUIRED
    val name: String,
    val ingredients: String,
    val calories: Int
)
