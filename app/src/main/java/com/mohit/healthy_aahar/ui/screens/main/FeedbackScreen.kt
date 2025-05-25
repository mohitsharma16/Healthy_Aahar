package com.mohit.healthy_aahar.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mohit.healthy_aahar.R
import com.mohit.healthy_aahar.ui.theme.Primary600

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(navController: NavController) {
    // Static data for the UI
    val recipes = listOf("Scrambled eggs", "Chicken over rice", "Caesar salad")

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
                    Spacer(modifier = Modifier.weight(1f))

                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    "Help us track you goals better.",
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

                // Recipe Rating Section - Static UI
                recipes.forEach { recipe ->
                    RecipeRatingItem(
                        recipeName = recipe,
                        rating = 0, // Default to no stars selected
                        onRatingChanged = { } // No-op for static UI
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Have you followed the diet we recommended?",
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Meal Tracking Section - Static UI
                listOf("Breakfast", "Lunch", "Dinner").forEach { meal ->
                    MealTrackingItem(
                        mealName = meal,
                        isFollowed = false, // Default to unchecked
                        onYesClicked = { }, // No-op for static UI
                        onNoClicked = { }   // No-op for static UI
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Save Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { /* Static UI - no action */ },
                    modifier = Modifier
                        .widthIn(min = 120.dp)
                        .height(48.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A))
                ) {
                    Text(
                        text = "Save",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }

        }
    }
}

@Composable
fun RecipeRatingItem(
    recipeName: String,
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {
    Column {
        Text(
            text = recipeName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row {
            for (i in 1..5) {
                Icon(
                    imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Star $i",
                    tint = if (i <= rating) Color(0xFFFFBB0E) else Color.LightGray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onRatingChanged(i) }
                        .padding(end = 4.dp)
                )
            }
        }
    }
}

@Composable
fun MealTrackingItem(
    mealName: String,
    isFollowed: Boolean,
    onYesClicked: () -> Unit,
    onNoClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = mealName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(100.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = isFollowed,
                onClick = onYesClicked,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF8BC34A)
                )
            )
            Text(
                text = "Yes",
                modifier = Modifier.clickable { onYesClicked() }
            )

            Spacer(modifier = Modifier.width(24.dp))

            RadioButton(
                selected = !isFollowed,
                onClick = onNoClicked,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF8BC34A)
                )
            )
            Text(
                text = "No",
                modifier = Modifier.clickable { onNoClicked() }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewFeedbackScreen() {
    FeedbackScreen(navController = rememberNavController())
}