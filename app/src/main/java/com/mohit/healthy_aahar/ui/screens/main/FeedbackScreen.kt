package com.mohit.healthy_aahar.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mohit.healthy_aahar.ui.theme.Primary600
import com.mohit.healthy_aahar.ui.viewmodel.MainViewModel
import com.mohit.healthy_aahar.model.Meal

data class RecipeFeedback(
    val recipeId: String,
    val recipeName: String,
    val rating: Int = 0,
    val comment: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    navController: NavController,
    viewModel: MainViewModel,
    uid: String
) {
    // Observe meal plan from ViewModel
    val mealPlan by viewModel.mealPlan.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    val registerResponse by viewModel.registerResponse.observeAsState()

    // State for feedback data
    var feedbackList by remember { mutableStateOf(listOf<RecipeFeedback>()) }
    var isSubmitting by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    // Initialize feedback list when meal plan is loaded
    LaunchedEffect(mealPlan) {
        mealPlan?.let { plan ->
            val recipes = plan.meal_plan.map { meal ->
                RecipeFeedback(
                    recipeId = meal._id,
                    recipeName = meal.TranslatedRecipeName
                )
            }
            feedbackList = recipes
        }
    }

    // Load meal plan if not already loaded
    LaunchedEffect(uid) {
        if (mealPlan == null && uid.isNotEmpty()) {
            viewModel.getMealPlan(uid)
        }
    }

    // Show success message
    LaunchedEffect(registerResponse) {
        registerResponse?.let {
            if (it.contains("successfully")) {
                showSuccessMessage = true
                isSubmitting = false
                viewModel.clearMessages()
                // Hide success message after 3 seconds
                kotlinx.coroutines.delay(3000)
                showSuccessMessage = false
            }
        }
    }

    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            // Top Bar with green background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Primary600)
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.clickable { navController.popBackStack() }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Feedback",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary600)
                }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Text(
                        "Help us track your goals better.",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Rate the recipes we recommended you.",
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Show success message
                    if (showSuccessMessage) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8))
                        ) {
                            Text(
                                text = "Feedback submitted successfully! Thank you for helping us improve.",
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Show error if any
                    error?.let {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                        ) {
                            Text(
                                text = it,
                                color = Color.Red,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Handle case when uid is empty
                    if (uid.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                        ) {
                            Text(
                                text = "Please log in to provide feedback.",
                                color = Color(0xFFE65100),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Recipe Rating Section
                    if (feedbackList.isEmpty() && mealPlan == null && uid.isNotEmpty()) {
                        Text(
                            "No recipes found. Please generate a meal plan first.",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else if (uid.isNotEmpty()) {
                        feedbackList.forEachIndexed { index, feedback ->
                            RecipeRatingItem(
                                recipeName = feedback.recipeName,
                                rating = feedback.rating,
                                comment = feedback.comment,
                                onRatingChanged = { newRating ->
                                    feedbackList = feedbackList.toMutableList().apply {
                                        this[index] = this[index].copy(rating = newRating)
                                    }
                                },
                                onCommentsChanged = { newComment ->
                                    feedbackList = feedbackList.toMutableList().apply {
                                        this[index] = this[index].copy(comment = newComment)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Save Button
                if (uid.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                isSubmitting = true
                                // Submit feedback for each rated recipe
                                feedbackList.forEach { feedback ->
                                    if (feedback.rating > 0) {
                                        viewModel.submitRecipeFeedback(
                                            uid = uid,
                                            recipeId = feedback.recipeId,
                                            rating = feedback.rating,
                                            comments = feedback.comment
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .widthIn(min = 120.dp)
                                .height(48.dp),
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A)),
                            enabled = !isSubmitting && feedbackList.any { it.rating > 0 }
                        ) {
                            if (isSubmitting) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text(
                                    text = "Save Feedback",
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeRatingItem(
    recipeName: String,
    rating: Int,
    comment: String,
    onRatingChanged: (Int) -> Unit,
    onCommentsChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = recipeName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Star Rating
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rating:",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.width(60.dp)
                )

                Row {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Star $i",
                            tint = if (i <= rating) Color(0xFFFFBB0E) else Color.LightGray,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable { onRatingChanged(i) }
                                .padding(end = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Comments Section
            Text(
                text = "Comments (Optional):",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = comment,
                onValueChange = onCommentsChanged,
                placeholder = { Text("Share your thoughts about this recipe...") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary600,
                    cursorColor = Primary600
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFeedbackScreen() {
    // Preview with mock data - you'll need to provide actual instances for preview
}