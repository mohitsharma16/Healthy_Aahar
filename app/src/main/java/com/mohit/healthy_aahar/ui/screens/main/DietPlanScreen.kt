package com.mohit.healthy_aahar.ui.screens.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.mohit.healthy_aahar.model.Meal
import com.mohit.healthy_aahar.ui.navigation.Screen
import com.mohit.healthy_aahar.ui.viewmodel.MainViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mohit.healthy_aahar.datastore.UserPreference
import com.mohit.healthy_aahar.model.LogMealRequest


@Composable
fun DietPlanScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val mainViewModel: MainViewModel = viewModel()
    val mealPlanState by mainViewModel.mealPlan.observeAsState()
    // Trigger once on first composition
    LaunchedEffect(Unit) {
        mainViewModel.getMealPlan("sourabh aharma") // Replace with actual user name
    }


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
            0 -> PersonalizedDietPlan(mealPlanState?.meal_plan ?: emptyList())
            1 -> DailyNutritionOverview()
            2 -> MealLoggingForm()
        }
    }
}

// **Tab Component**
@Composable
fun DietPlanTab(icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit, ) {

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
fun PersonalizedDietPlan(mealPlan: List<Meal>) {
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
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    NutrientCard("Carbs", "0")
                    NutrientCard("Protein", "0")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    NutrientCard("Water", "0")
                    NutrientCard("Calorie", "0")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Recommended Recipes", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        mealPlan.forEach { meal ->
            MealCard(meal)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun MealCard(meal: Meal) {
    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current
    val uidFlow = remember { UserPreference.getUidFlow(context) }
    val uid by uidFlow.collectAsState(initial = null)

    val today = remember { java.time.LocalDate.now().toString() }



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF6EC))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(meal.TranslatedRecipeName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Cuisine: ${meal.Cuisine}")
            Text("Time: ${meal.TotalTimeInMins} mins")
            Spacer(modifier = Modifier.height(4.dp))
            Text("🔥 ${meal.Calories} kcal | 🥩 ${meal.Protein}g | 🧈 ${meal.Fat}g | 🍞 ${meal.Carbs}g")
            Spacer(modifier = Modifier.height(8.dp))
            Text(meal.TranslatedInstructions.orEmpty(), fontSize = 13.sp, color = Color.Gray, maxLines = 3, overflow = TextOverflow.Ellipsis)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // ✅ Log Button
                Button(
                    onClick = {
                        uid?.let {
                            // Call ViewModel function and show Toast after success
                            viewModel.logMeal(it, meal._id, today) { success ->
                                val msg = if (success) "Meal logged successfully!" else "Failed to log meal"
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8D4A1D))
                ) {
                    Text("Log Meal", color = Color.White)
                }

                // 🔁 Swap Icon
//                IconButton(onClick = {
//                    viewModel.swapMeal(mealIndex = mealPlanIndex(meal)) // implement this part
//                }) {
//                    Icon(Icons.Default.Restaurant, contentDescription = "Swap Meal")
//                }
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
    val context = LocalContext.current
    val mainViewModel: MainViewModel = viewModel()
    val uidFlow = remember { UserPreference.getUidFlow(context) }
    val uid by uidFlow.collectAsState(initial = null)

    var customMealText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8D6BE), shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Log Your Custom Meal", fontSize = 20.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = customMealText,
            onValueChange = { customMealText = it },
            label = { Text("Enter your meal") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF8D4A1D),
                focusedLabelColor = Color(0xFF8D4A1D)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (!customMealText.isNullOrBlank() && uid != null) {
                    mainViewModel.logCustomMeal(
                        uid = uid!!,
                        date = java.time.LocalDate.now().toString(),
                        description = customMealText.trim()
                    )
                    Toast.makeText(context, "Logging your meal...", Toast.LENGTH_SHORT).show()
                    customMealText = ""
                } else {
                    Toast.makeText(context, "Please enter a meal", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8D4A1D))
        ) {
            Text("Log", color = Color.White)
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
//@Preview(showBackground = true)
//@Composable
//fun DietPlanScreenPreview() {
//    val navController = rememberNavController()
//    DietPlanScreen(navController)
//}