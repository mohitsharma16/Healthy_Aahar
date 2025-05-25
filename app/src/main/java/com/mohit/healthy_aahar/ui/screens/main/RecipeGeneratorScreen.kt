package com.mohit.healthy_aahar.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mohit.healthy_aahar.R
import com.mohit.healthy_aahar.datastore.UserPreference
import com.mohit.healthy_aahar.ui.navigation.Screen
import com.mohit.healthy_aahar.ui.theme.Primary600
import com.mohit.healthy_aahar.ui.theme.Primary700
import com.mohit.healthy_aahar.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeGeneratorScreen(navController: NavController, onMenuClick: () -> Unit) {
    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current
    val uidFlow = remember { UserPreference.getUidFlow(context) }
    val uid by uidFlow.collectAsState(initial = null)

    var selectedItems by remember { mutableStateOf(setOf<String>()) }
    var searchText by remember { mutableStateOf("") }
    val recipe by viewModel.generatedRecipe.observeAsState()
    val error by viewModel.error.observeAsState()
    val isLoading = remember { mutableStateOf(false) }

    val foodItems = listOf(
        FoodItem("Corn", R.drawable.ic_corn),
        FoodItem("Carrot", R.drawable.carrot),
        FoodItem("Meat", R.drawable.meat),
        FoodItem("Chicken", R.drawable.chicken),
        FoodItem("Eggs", R.drawable.egg),
        FoodItem("Wheat", R.drawable.sweet_potato), // Using sweet_potato as placeholder for wheat
        FoodItem("Cabbage", R.drawable.cabbage),
        FoodItem("Broccoli", R.drawable.broccoli),
        FoodItem("Lettuce", R.drawable.lettuce)
    )

    val filteredItems = if (searchText.isEmpty()) {
        foodItems
    } else {
        foodItems.filter { it.name.contains(searchText, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
                    text = "Recipe Generator",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onMenuClick() }
                )
            }
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Search Ingredients") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(0xFF8BC34A)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Pick your ingredients",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ingredient Grid - 2 columns
            val chunkedItems = filteredItems.chunked(2)
            chunkedItems.forEach { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowItems.forEach { item ->
                        val isSelected = selectedItems.contains(item.name)
                        IngredientCard(
                            item = item,
                            isSelected = isSelected,
                            onClick = {
                                selectedItems = if (isSelected) {
                                    selectedItems - item.name
                                } else {
                                    selectedItems + item.name
                                }
                            }
                        )
                    }
                    // If odd number of items in last row, add spacer
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Generate Button
            Button(
                onClick = {
                    val ingredients = selectedItems.map { it.lowercase() }
                    if (uid != null && ingredients.isNotEmpty()) {
                        isLoading.value = true
                        viewModel.generateRecipeByIngredients(uid!!, ingredients)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                enabled = selectedItems.isNotEmpty()
            ) {
                if (isLoading.value) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Generating...",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Text(
                        text = "Generate Recipe",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recipe Result - appears below the button
            if (recipe != null) {
                RecipeResultCard(recipe!!)
            }

            if (error != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("❌", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = error!!,
                            color = Color(0xFFD32F2F),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Bottom padding for better scrolling
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun IngredientCard(item: FoodItem, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(if (isSelected) 6.dp else 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = if (isSelected) 3.dp else 2.dp,
                    color = if (isSelected) Color(0xFF2E7D32) else Color(0xFF2E7D32).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                )
                .background(
                    color = if (isSelected) Color(0xFF2E7D32).copy(alpha = 0.05f) else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.name,
                    modifier = Modifier.size(48.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.name,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    color = if (isSelected) Color(0xFF2E7D32) else Color(0xFF424242)
                )
            }
        }
    }
}

@Composable
fun RecipeResultCard(recipe: Any) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header with recipe title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🍽️",
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Generated Recipe",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Using reflection to get properties to maintain compatibility with original code
            val recipeName = try {
                recipe.javaClass.getDeclaredField("TranslatedRecipeName").apply { isAccessible = true }.get(recipe)?.toString() ?: "Delicious Recipe"
            } catch (e: Exception) {
                "Delicious Recipe"
            }

            val cuisine = try {
                recipe.javaClass.getDeclaredField("Cuisine").apply { isAccessible = true }.get(recipe)?.toString() ?: "Mixed"
            } catch (e: Exception) {
                "Mixed"
            }

            val time = try {
                recipe.javaClass.getDeclaredField("TotalTimeInMins").apply { isAccessible = true }.get(recipe)?.toString() ?: "30"
            } catch (e: Exception) {
                "30"
            }

            val calories = try {
                recipe.javaClass.getDeclaredField("Calories").apply { isAccessible = true }.get(recipe)?.toString() ?: "N/A"
            } catch (e: Exception) {
                "N/A"
            }

            val ingredients = try {
                recipe.javaClass.getDeclaredField("TranslatedIngredients").apply { isAccessible = true }.get(recipe)?.toString() ?: ""
            } catch (e: Exception) {
                ""
            }

            val instructions = try {
                recipe.javaClass.getDeclaredField("TranslatedInstructions").apply { isAccessible = true }.get(recipe)?.toString() ?: ""
            } catch (e: Exception) {
                ""
            }

            // Recipe Name
            Text(
                text = recipeName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Recipe Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoChip(icon = "🌍", label = "Cuisine", value = cuisine)
                InfoChip(icon = "🕒", label = "Time", value = "$time mins")
                InfoChip(icon = "🔥", label = "Calories", value = calories)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ingredients Section
            SectionCard(
                title = "📋 Ingredients",
                content = ingredients
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Instructions Section
            SectionCard(
                title = "📖 Instructions",
                content = instructions
            )
        }
    }
}

@Composable
fun InfoChip(icon: String, label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = icon, fontSize = 20.sp)
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2E7D32)
        )
    }
}

@Composable
fun SectionCard(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF2E7D32)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                fontSize = 14.sp,
                color = Color(0xFF424242),
                lineHeight = 20.sp
            )
        }
    }
}

// Data Class for Food Items
data class FoodItem(val name: String, val imageRes: Int)

@Preview(showBackground = true)
@Composable
fun PreviewRecipeGeneratorScreen() {
    RecipeGeneratorScreen(navController = rememberNavController(), onMenuClick = {})
}