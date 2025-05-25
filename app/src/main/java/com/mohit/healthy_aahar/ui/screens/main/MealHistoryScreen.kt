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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class MealItem(
    val id: Int,
    val name: String,
    val mealType: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val imageRes: Int, // You'll need to add actual image resources
    val isFavorite: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealHistoryScreen(navController: NavController) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Breakfast", "Lunch", "Dinner")

    // Sample data - replace with your actual data source
    val sampleMeals = listOf(
        MealItem(1, "Scrambled Eggs", "Breakfast", 451, 25, 32, 14, android.R.drawable.ic_menu_gallery, true),
        MealItem(2, "Scrambled Eggs", "Breakfast", 451, 25, 32, 14, android.R.drawable.ic_menu_gallery),
        MealItem(3, "Scrambled Eggs", "Breakfast", 451, 25, 32, 14, android.R.drawable.ic_menu_gallery),
        MealItem(4, "Grilled Chicken", "Lunch", 320, 35, 5, 12, android.R.drawable.ic_menu_gallery),
        MealItem(5, "Caesar Salad", "Lunch", 280, 8, 15, 22, android.R.drawable.ic_menu_gallery),
        MealItem(6, "Salmon Dinner", "Dinner", 420, 40, 8, 25, android.R.drawable.ic_menu_gallery),
    )

    val filteredMeals = sampleMeals.filter { meal ->
        meal.mealType.equals(tabs[selectedTabIndex], ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Past Recipes", color = Color.White, fontWeight = FontWeight.Medium) },
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
                    IconButton(onClick = { /* Handle menu */ }) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF8BC34A) // Green color from the image
                )
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = Color(0xFF8BC34A),
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = Color(0xFF8BC34A),
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
                                color = if (selectedTabIndex == index) Color(0xFF8BC34A) else Color.Gray
                            )
                        }
                    )
                }
            }

            // Meal List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredMeals) { meal ->
                    MealCard(meal = meal)
                }
            }
        }
    }
}

@Composable
fun MealCard(meal: MealItem) {
    var isFavorite by remember { mutableStateOf(meal.isFavorite) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Meal Image
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F5F5))
            ) {
                // Replace with actual image loading (Coil, Glide, etc.)
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
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = meal.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = meal.mealType,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "${meal.calories} kcal",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    // Favorite Icon
                    IconButton(
                        onClick = { isFavorite = !isFavorite },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color(0xFF8BC34A) else Color.Gray,
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
                        color = Color(0xFF8BC34A)
                    )
                    NutritionItem(
                        value = "${meal.carbs}g",
                        label = "Carbs",
                        color = Color(0xFF8BC34A)
                    )
                    NutritionItem(
                        value = "${meal.fats}g",
                        label = "Fats",
                        color = Color(0xFF8BC34A)
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