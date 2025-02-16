package com.mohit.healthy_aahar.model

data class User(
    val id: Int,  // 🔥 PRIMARY KEY IS REQUIRED
    val name: String,
    val age: Int,
    val weight: Float,
    val height: Float
)
