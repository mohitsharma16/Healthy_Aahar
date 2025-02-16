package com.mohit.healthy_aahar.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mohit.healthy_aahar.ui.navigation.Screen

@Composable
fun DietPlanScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // **Top Tab Navigation**
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DietPlanTab(
                icon = Icons.Filled.Restaurant,
                isSelected = selectedTab == 0,
                onClick = { selectedTab = 0 }
            )
            DietPlanTab(
                icon = Icons.Filled.Schedule,
                isSelected = selectedTab == 1,
                onClick = { selectedTab = 1 }
            )
            DietPlanTab(
                icon = Icons.Filled.BarChart,
                isSelected = selectedTab == 2,
                onClick = { selectedTab = 2 }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // **Display Selected Content Based on Tab**
        when (selectedTab) {
            0 -> PersonalizedDietPlan()
            1 -> DailyNutritionOverview()
            2 -> MealLoggingForm()
        }
    }
}

// **Tab Component**
@Composable
fun DietPlanTab(icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Color(0xFF8D4A1D) else Color.Gray,
            modifier = Modifier.size(32.dp)
        )
    }
}

// **Tab 1: Personalized Diet Plan**
@Composable
fun PersonalizedDietPlan() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Your Personalized Diet Plan", fontSize = 20.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DietPlanCard("MUSCLE GAIN", Color(0xFFD69AAE))
            DietPlanCard("Keto", Color(0xFFF0E68C))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Calorie Target", fontSize = 18.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFC5E6DA), shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutrientCard("Carbs", "0")
                NutrientCard("Protein", "0")
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 50.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutrientCard("Water", "0")
                NutrientCard("Calorie", "0")
            }
        }
    }
}

// **Tab 2: Daily Nutrition Overview**
@Composable
fun DailyNutritionOverview() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Complete Your Daily Nutrition", fontSize = 20.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NutritionInfoCard("Kalori", "832kCal")
            NutritionInfoCard("Protein", "200gr")
            NutritionInfoCard("Water", "1000ml")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Have you followed your diet today?", fontSize = 18.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(12.dp))

        Column {
            Row {
                CheckboxItem("Breakfast")
                CheckboxItem("Lunch")
            }
            Row {
                CheckboxItem("Dinner")
                CheckboxItem("Water")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("If Not, Then Log Your Meal", fontSize = 16.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(8.dp))

        FloatingActionButton(onClick = { /* TODO: Navigate to Meal Log */ }) {
            Text("+", fontSize = 24.sp)
        }
    }
}

// **Tab 3: Meal Logging Form**
@Composable
fun MealLoggingForm() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8D6BE), shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Log Your Meals", fontSize = 20.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(12.dp))

        MealInputField("Breakfast")
        MealInputField("Lunch")
        MealInputField("Dinner")

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { /* TODO: Log Meals */ }) {
            Text("Log")
        }
    }
}

// **Reusable Components**
@Composable
fun DietPlanCard(text: String, color: Color) {
    Box(
        modifier = Modifier
            .size(120.dp, 60.dp)
            .background(color, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

@Composable
fun NutrientCard(title: String, value: String) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, fontSize = 14.sp, color = Color.Black)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

@Composable
fun NutritionInfoCard(title: String, value: String) {
    Box(
        modifier = Modifier
            .size(100.dp, 60.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, fontSize = 14.sp, color = Color.Black)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

@Composable
fun CheckboxItem(label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = false, onCheckedChange = {})
        Text(label, fontSize = 16.sp, color = Color.Black)
    }
}

@Composable
fun MealInputField(label: String) {
    Column {
        Text(label, fontSize = 16.sp, color = Color.Black)
        TextField(value = "", onValueChange = {})
    }
}
@Preview(showBackground = true)
@Composable
fun DietPlanScreenPreview() {
    val navController = rememberNavController()
    DietPlanScreen(navController)
}