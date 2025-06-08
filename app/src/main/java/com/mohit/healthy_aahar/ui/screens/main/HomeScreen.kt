package com.mohit.healthy_aahar.ui.screens.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.mohit.healthy_aahar.model.DailyNutrition
import com.mohit.healthy_aahar.ui.navigation.Screen
import com.mohit.healthy_aahar.ui.theme.GreenBackground
import com.mohit.healthy_aahar.ui.theme.LightGreen
import com.mohit.healthy_aahar.ui.theme.Primary200
import com.mohit.healthy_aahar.ui.theme.Primary50
import com.mohit.healthy_aahar.ui.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(navController: NavController, onMenuClick: () -> Unit, viewModel: MainViewModel = viewModel() ) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val uidFlow = remember { UserPreference.getUidFlow(context) }
    val uid by uidFlow.collectAsState(initial = null)
    val mealPlan by viewModel.mealPlan.observeAsState()
    val dailyNutrition by viewModel.dailyNutrition.observeAsState()
    val userDetails by viewModel.userDetails.observeAsState()
    val error by viewModel.error.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    // Get current date for API calls
    val currentDate = remember { LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) }

    // Trigger the API calls when the screen loads
    LaunchedEffect(uid) {
        uid?.let { userId ->
            viewModel.getMealPlan(userId)
            viewModel.fetchDailyNutrition(userId, currentDate)
            viewModel.fetchUserDetails(userId) { }
        }
    }

    // Refresh daily nutrition when meals are updated (you can call this from meal logging)
    fun refreshNutritionData() {
        uid?.let { userId ->
            viewModel.fetchDailyNutrition(userId, currentDate)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
    ) {
        // Welcome Header
        WelcomeHeader(onMenuClick , userDetails)

        // Daily Tracker Section with real data
        DailyTrackerSectionWithRealData(
            dailyNutrition = dailyNutrition,
            userDetails = userDetails,
            isLoading = isLoading
        )

        // Today's Meals Section
        TodaysMealsSectionWithSkeleton(
            navController = navController,
            meals = mealPlan?.meal_plan ?: emptyList(),
            isLoading = isLoading,
            onMealConsumed = { refreshNutritionData() } // Refresh when meal is consumed
        )

        // Bottom Navigation Spacer (to account for the bottom nav bar)
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun DailyTrackerSectionWithRealData(
    dailyNutrition: DailyNutrition?,
    userDetails: com.mohit.healthy_aahar.model.UserDetails?, // Add your UserDetails model
    isLoading: Boolean
) {
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

        if (isLoading) {
            // Show skeleton loading for nutrition cards
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(3) {
                    NutritionTrackerCardSkeleton(
                        modifier = Modifier.width(120.dp)
                    )
                }
            }
        } else {
            // Get actual nutrition data
            val actualCalories = dailyNutrition?.total?.calories ?: 0
            val actualProtein = dailyNutrition?.total?.protein ?: 0
            val actualCarbs = dailyNutrition?.total?.carbs ?: 0
            val actualFat = dailyNutrition?.total?.fat ?: 0

            // Calculate targets based on user's BMR or use defaults
            val targetCalories = userDetails?.bmr?.toInt() ?: 2050
            val targetProtein = ((targetCalories * 0.2) / 4).toInt() // 20% from protein
            val targetCarbs = ((targetCalories * 0.5) / 4).toInt() // 50% from carbs
            val targetFat = ((targetCalories * 0.3) / 9).toInt() // 30% from fat

            // Calculate progress
            val calorieProgress = if (targetCalories > 0) (actualCalories.toFloat() / targetCalories).coerceIn(0f, 1f) else 0f
            val proteinProgress = if (targetProtein > 0) (actualProtein.toFloat() / targetProtein).coerceIn(0f, 1f) else 0f
            val carbsProgress = if (targetCarbs > 0) (actualCarbs.toFloat() / targetCarbs).coerceIn(0f, 1f) else 0f
            val fatProgress = if (targetFat > 0) (actualFat.toFloat() / targetFat).coerceIn(0f, 1f) else 0f

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                item {
                    // Calorie Tracker Card
                    NutritionTrackerCard(
                        label = "Calorie",
                        icon = R.drawable.ic_fire,
                        currentValue = actualCalories.toString(),
                        targetValue = targetCalories.toString(),
                        unit = "kcal",
                        progress = calorieProgress,
                        modifier = Modifier.width(120.dp)
                    )
                }

                item {
                    // Protein Tracker Card
                    NutritionTrackerCard(
                        label = "Protein",
                        icon = R.drawable.ic_protein,
                        currentValue = actualProtein.toString(),
                        targetValue = targetProtein.toString(),
                        unit = "gm",
                        progress = proteinProgress,
                        modifier = Modifier.width(120.dp)
                    )
                }

                item {
                    // Carbs Tracker Card
                    NutritionTrackerCard(
                        label = "Carbs",
                        icon = null,
                        currentValue = actualCarbs.toString(),
                        targetValue = targetCarbs.toString(),
                        unit = "gm",
                        progress = carbsProgress,
                        modifier = Modifier.width(120.dp)
                    )
                }

                item {
                    // Fat Tracker Card
                    NutritionTrackerCard(
                        label = "Fat",
                        icon = null,
                        currentValue = actualFat.toString(),
                        targetValue = targetFat.toString(),
                        unit = "gm",
                        progress = fatProgress,
                        modifier = Modifier.width(120.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NutritionTrackerCardSkeleton(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Primary50)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Label skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(14.dp)
                    .background(
                        Color.LightGray.copy(alpha = 0.3f),
                        RoundedCornerShape(4.dp)
                    )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Value skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(16.dp)
                    .background(
                        Color.LightGray.copy(alpha = 0.3f),
                        RoundedCornerShape(4.dp)
                    )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Unit skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(14.dp)
                    .background(
                        Color.LightGray.copy(alpha = 0.2f),
                        RoundedCornerShape(4.dp)
                    )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Progress bar skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        Color.LightGray.copy(alpha = 0.3f),
                        RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}

// Keep your existing NutritionTrackerCard composable as is

@Composable
fun WelcomeHeader(onMenuClick: () -> Unit , userDetails: com.mohit.healthy_aahar.model.UserDetails?,) {

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
                text = "Hi, ${userDetails?.name}.",
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

// Updated TodaysMealsSection with callback for meal consumption
@Composable
fun TodaysMealsSectionWithSkeleton(
    navController: NavController,
    meals: List<Meal>,
    isLoading: Boolean = false,
    onMealConsumed: () -> Unit = {} // Callback when meal is consumed
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

        when {
            isLoading -> {
                // Show skeleton cards
                Column {
                    repeat(3) { // Show 3 skeleton cards
                        MealCardSkeleton()
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
            meals.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🍽️",
                            fontSize = 32.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No meals planned for today",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
            else -> {
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
                            onClick = {
                                navController.navigate("${Screen.NutritionalAnalysis.route}/${meal._id}")
                            },
                            onMealConsumed = onMealConsumed // Pass the callback
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
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
    onClick: () -> Unit,
    onMealConsumed: () -> Unit = {} // Add callback parameter
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
fun MealCardSkeleton() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8FCF8)
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Title placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(16.dp)
                        .background(
                            Color.LightGray.copy(alpha = 0.3f),
                            RoundedCornerShape(4.dp)
                        )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(12.dp)
                        .background(
                            Color.LightGray.copy(alpha = 0.2f),
                            RoundedCornerShape(4.dp)
                        )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Nutrition info placeholders
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(3) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(12.dp)
                                    .background(
                                        Color.LightGray.copy(alpha = 0.2f),
                                        RoundedCornerShape(4.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(10.dp)
                                    .background(
                                        Color.LightGray.copy(alpha = 0.15f),
                                        RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                    }
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

@Preview(showBackground = true)
@Composable
fun PreviewFullHomeScreen() {
    HomeScreen(
        navController = rememberNavController(),
        onMenuClick = {}
    )
}