package com.mohit.healthy_aahar.model

data class UserDetails(
    val uid: String,
    val name: String,
    val age: Int,
    val gender: String,
    val weight: Float,
    val height: Float,
    val activity_level: String,
    val goal: String,
    val bmr: Float,
    val tdee: Float
)
