package com.mohit.healthy_aahar.ui.screens.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mohit.healthy_aahar.R
import com.mohit.healthy_aahar.datastore.UserPreference
import com.mohit.healthy_aahar.model.Meal
import com.mohit.healthy_aahar.ui.navigation.Screen
import com.mohit.healthy_aahar.ui.theme.GreenBackground
import com.mohit.healthy_aahar.ui.theme.LightGreen
import com.mohit.healthy_aahar.ui.theme.Primary200
import com.mohit.healthy_aahar.ui.theme.Primary50
import com.mohit.healthy_aahar.ui.viewmodel.MainViewModel

@Composable
fun HomeScreen(navController: NavController, onMenuClick: () -> Unit, viewModel: MainViewModel = viewModel() ) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val uidFlow = remember { UserPreference.getUidFlow(context) }
    val uid by uidFlow.collectAsState(initial = null)
    val mealPlan by viewModel.mealPlan.observeAsState()
    val error by viewModel.error.observeAsState()

    // Trigger the API call only once when the screen loads
    LaunchedEffect(uid) {
        uid?.let { viewModel.getMealPlan(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
    ) {
        // Welcome Header
        WelcomeHeader(onMenuClick)

        // Daily Tracker Section
        DailyTrackerSection()

        // Today's Meals Section
        TodaysMealsSection(navController, meals = mealPlan?.meal_plan ?: emptyList())

        // Bottom Navigation Spacer (to account for the bottom nav bar)
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun WelcomeHeader(onMenuClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
        colors = CardDefaults.cardColors(containerColor = GreenBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Welcome Back",
                    color = Color.White,
                    fontSize = 16.sp,
                )

                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onMenuClick() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Hi, Mohit.",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Small changes today.",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Stronger you tomorrow.",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun DailyTrackerSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Daily Tracker",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Calorie Tracker Card
            NutritionTrackerCard(
                label = "Calorie",
                icon = R.drawable.ic_fire,
                currentValue = "451",
                targetValue = "2050",
                unit = "kcal",
                progress = 0.22f,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Protein Tracker Card
            NutritionTrackerCard(
                label = "Protein",
                icon = R.drawable.ic_protein,
                currentValue = "45",
                targetValue = "500",
                unit = "gm",
                progress = 0.09f,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Carbs Tracker Card
            NutritionTrackerCard(
                label = "Carbs",
                icon = null,
                currentValue = "281",
                targetValue = "356",
                unit = "gm",
                progress = 0.79f,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun NutritionTrackerCard(
    label: String,
    icon: Int?,
    currentValue: String,
    targetValue: String,
    unit: String,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Primary50
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                if (icon != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = LightGreen,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$currentValue/$targetValue",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = unit,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = LightGreen,
                trackColor = Color.LightGray
            )
        }
    }
}

@Composable
fun TodaysMealsSection(
    navController: NavController,
    meals: List<Meal>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Today's Meals",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (meals.isEmpty()) {
            Text(text = "No meals planned for today.")
        } else {
            Column {
                meals.forEach { meal ->
                    MealCard(
                        mealName = meal.TranslatedRecipeName,
                        mealTime = "",
                        calories = meal.Calories.toInt().toString(),
                        protein = meal.Protein.toString(),
                        carbs = meal.Carbs.toString(),
                        fats = meal.Fat.toString(),
                        imageRes = R.drawable.ic_food_placeholder,
                        // Pass the recipe ID when navigating
                        onClick = {
                            navController.navigate("${Screen.NutritionalAnalysis.route}/${meal._id}")
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun MealCard(
    mealName: String,
    mealTime: String,
    calories: String,
    protein: String,
    carbs: String,
    fats: String,
    imageRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8FCF8) // Very light green background
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Meal Image
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = mealName,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = mealName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = mealTime,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Options",
                    tint = Color.Gray
                )
            }

            if (calories.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_fire),
                        contentDescription = null,
                        tint = LightGreen,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "$calories kcal",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            if (protein.isNotEmpty() && carbs.isNotEmpty() && fats.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    NutrientInfo(label = "Protein", value = protein)
                    NutrientInfo(label = "Carbs", value = carbs)
                    NutrientInfo(label = "Fats", value = fats)
                }
            }
        }
    }
}

@Composable
fun NutrientInfo(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun MealSwapTestScreen(userName: String) {
    val viewModel: MainViewModel = viewModel()
    val registerResponse by viewModel.registerResponse.observeAsState()
    val error by viewModel.error.observeAsState()

    LaunchedEffect(registerResponse) {
        registerResponse?.let {
            Log.d("SWAP_TEST", it)
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(error) {
        error?.let {
            Log.e("SWAP_TEST", "Error: $it")
            viewModel.clearMessages()
        }
    }

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(32.dp))
    }
}

// Add to your theme file:
// val GreenBackground = Color(0xFF8EB989)
// val LightGreen = Color(0xFF8EB989)

@Preview(showBackground = true)
@Composable
fun PreviewFullHomeScreen() {
    HomeScreen(
        navController = rememberNavController(),
        onMenuClick = {}
    )
}