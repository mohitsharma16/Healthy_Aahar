package com.mohit.healthy_aahar.model

data class FeedbackRequest(
    val recipe_id: String,
    val rating: Int,
    val comments: String?
)
