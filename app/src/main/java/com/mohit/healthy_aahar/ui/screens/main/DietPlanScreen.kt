package com.mohit.healthy_aahar.ui.screens.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Menu
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietPlanScreen(navController: NavController, onMenuClick: () -> Unit) {
    val mainViewModel: MainViewModel = viewModel()
    val mealPlanState by mainViewModel.mealPlan.observeAsState()
    val context = LocalContext.current
    val uidFlow = remember { UserPreference.getUidFlow(context) }
    val uid by uidFlow.collectAsState(initial = null)

    // Trigger once on first composition
    LaunchedEffect(uid) {
        uid?.let { mainViewModel.getMealPlan(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // White background
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    "Diet Plan",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            actions = {
                IconButton(onClick = { onMenuClick()}) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF8FBC8F)
            )
        )

        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White) // White background for content area
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Details Card
            UserDetailsCard()

            // Concern Details Card
            ConcernDetailsCard()

            // Other Details Card
            OtherDetailsCard()

            // Recommended Meals Section
            if (mealPlanState?.meal_plan?.isNotEmpty() == true) {
                RecommendedMealsSection(
                    meals = mealPlanState?.meal_plan ?: emptyList(),
                    viewModel = mainViewModel,
                    uid = uid
                )
            }
        }
    }
}

@Composable
fun UserDetailsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8F0)), // Light green background
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF8FBC8F)) // Green border
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "User details",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    UserDetailItem("Name:", "Mohit Sharma")
                    Spacer(modifier = Modifier.height(8.dp))
                    UserDetailItem("Age:", "21 year")
                    Spacer(modifier = Modifier.height(8.dp))
                    UserDetailItem("Weight:", "96Kgs")
                }

                Column(modifier = Modifier.weight(1f)) {
                    UserDetailItem("Gender:", "Male")
                    Spacer(modifier = Modifier.height(8.dp))
                    UserDetailItem("BMI:", "21")
                    Spacer(modifier = Modifier.height(8.dp))
                    UserDetailItem("Height:", "184cm")
                }
            }
        }
    }
}

@Composable
fun ConcernDetailsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8F0)), // Light green background
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF8FBC8F)) // Green border
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Concern details",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            UserDetailItem("Concern:", "Weight Loss")
        }
    }
}

@Composable
fun OtherDetailsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8F0)), // Light green background
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF8FBC8F)) // Green border
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Other Details",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            UserDetailItem("Medical Condition:", "Diabetes")
            Spacer(modifier = Modifier.height(8.dp))
            UserDetailItem("Time period:", "1 Month")
            Spacer(modifier = Modifier.height(8.dp))
            UserDetailItem("Type of Diet:", "Keto")
        }
    }
}

@Composable
fun UserDetailItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun RecommendedMealsSection(
    meals: List<Meal>,
    viewModel: MainViewModel,
    uid: String?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8F0)), // Light green background
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF8FBC8F)) // Green border
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Recommended Meals",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            meals.forEach { meal ->
                MealCard(meal = meal, viewModel = viewModel, uid = uid)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun MealCard(meal: Meal, viewModel: MainViewModel, uid: String?) {
    val context = LocalContext.current
    val today = remember { java.time.LocalDate.now().toString() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF6EC))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                meal.TranslatedRecipeName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                "Cuisine: ${meal.Cuisine}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Text(
                "Time: ${meal.TotalTimeInMins} mins",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "🔥 ${meal.Calories} kcal | 🥩 ${meal.Protein}g | 🧈 ${meal.Fat}g | 🍞 ${meal.Carbs}g",
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            meal.TranslatedInstructions?.let { instructions ->
                Text(
                    instructions,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Log Meal Button
            Button(
                onClick = {
                    uid?.let {
                        viewModel.logMeal(it, meal._id, today) { success ->
                            val msg = if (success) "Meal logged successfully!" else "Failed to log meal"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8FBC8F)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Log Meal",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

