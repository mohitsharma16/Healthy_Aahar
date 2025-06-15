package com.mohit.healthy_aahar.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mohit.healthy_aahar.ui.theme.Primary600
import com.mohit.healthy_aahar.ui.theme.Primary700
import com.mohit.healthy_aahar.ui.viewmodel.MainViewModel

data class MealItem(
    val id: Int,
    val name: String,
    val mealType: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val imageRes: Int,
    val isFavorite: Boolean = false,
    val date: String = "",
    val mealId: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealHistoryScreen(
    navController: NavController,
    uid: String,
    viewModel: MainViewModel = viewModel()
) {
    // Observe meal history from ViewModel
    val mealHistoryResponse by viewModel.mealHistoryResponse.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    // Tab state
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("All", "Breakfast", "Lunch", "Dinner", "Snacks")

    // Fetch meal history when screen loads
    LaunchedEffect(uid) {
        viewModel.fetchMealHistory(uid)
    }

    // Replace the allMealItems computation section (around line 70-85) with this:

// Convert MealHistoryResponse to MealItem for display
    val allMealItems = remember(mealHistoryResponse) {
        mealHistoryResponse?.flatMap { historyResponse ->
            historyResponse.meals.map { meal ->
                MealItem(
                    id = meal.meal_id?.hashCode() ?: meal.meal_name.hashCode(), // Handle null meal_id
                    name = meal.meal_name,
                    mealType = meal.meal_type ?: "Unknown", // Handle null meal_type
                    calories = meal.calories,
                    protein = meal.protein,
                    carbs = meal.carbs,
                    fats = meal.fat,
                    imageRes = android.R.drawable.ic_menu_gallery,
                    isFavorite = false,
                    date = historyResponse.date,
                    mealId = meal.meal_id ?: meal.meal_name // Use meal_name as fallback for mealId
                )
            }
        } ?: emptyList()
    }

    // Filter meals based on selected tab
    val filteredMeals = remember(allMealItems, selectedTabIndex) {
        when (selectedTabIndex) {
            0 -> allMealItems // All
            1 -> allMealItems.filter { it.mealType.equals("breakfast", ignoreCase = true) }
            2 -> allMealItems.filter { it.mealType.equals("lunch", ignoreCase = true) }
            3 -> allMealItems.filter { it.mealType.equals("dinner", ignoreCase = true) }
            4 -> allMealItems.filter { it.mealType.equals("snack", ignoreCase = true) || it.mealType.equals("snacks", ignoreCase = true) }
            else -> allMealItems
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meal History", color = Color.White, fontWeight = FontWeight.Medium) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary600
                )
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .background(color = Color.White)
        ) {
            // Tab Row - Scrollable
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = Primary600,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = Primary600,
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) Primary600 else Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    )
                }
            }

            // Content based on loading state
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Primary600)
                    }
                }
                error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Error loading meal history",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Red
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = error ?: "Unknown error",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.fetchMealHistory(uid) },
                                colors = ButtonDefaults.buttonColors(containerColor = Primary600)
                            ) {
                                Text("Retry", color = Color.White)
                            }
                        }
                    }
                }
                filteredMeals.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (selectedTabIndex == 0) "No meals logged yet" else "No ${tabs[selectedTabIndex].lowercase()} meals found",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (selectedTabIndex == 0) "Start logging your meals to see them here" else "Try selecting a different meal type",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredMeals) { meal ->
                            MealCard(
                                meal = meal,
                                onMealClick = { mealId ->
                                    navController.navigate("nutritional_analysis/$mealId")
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Clear error when leaving screen
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearMessages()
        }
    }
}

@Composable
fun MealCard(
    meal: MealItem,
    onMealClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isFavorite by remember { mutableStateOf(meal.isFavorite) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onMealClick(meal.mealId)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Meal Image
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F5F5))
            ) {
                Image(
                    painter = painterResource(id = meal.imageRes),
                    contentDescription = meal.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Meal Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = meal.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // Show meal type badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Primary600.copy(alpha = 0.1f),
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = meal.mealType.replaceFirstChar { it.uppercase() },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = Primary600,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${meal.calories} kcal",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        if (meal.date.isNotEmpty()) {
                            Text(
                                text = meal.date,
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    // Favorite Icon
                    IconButton(
                        onClick = { isFavorite = !isFavorite },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Primary600 else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Nutrition Info
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    NutritionItem(
                        value = "${meal.protein}g",
                        label = "Protein",
                        color = Primary600
                    )
                    NutritionItem(
                        value = "${meal.carbs}g",
                        label = "Carbs",
                        color = Primary600
                    )
                    NutritionItem(
                        value = "${meal.fats}g",
                        label = "Fats",
                        color = Primary600
                    )
                }
            }
        }
    }
}

@Composable
fun NutritionItem(
    value: String,
    label: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(16.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}