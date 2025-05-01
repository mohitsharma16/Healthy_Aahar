package com.mohit.healthy_aahar.ui.screens.main

import androidx.compose.foundation.background
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
import com.mohit.healthy_aahar.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeGeneratorScreen(navController: NavController) {
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
                    .background(Color(0xFF8BC34A))
                    .padding(16.dp)
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
                        text = "LeftOvers",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            }

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
                    "Pick your Leftovers",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
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

                Spacer(modifier = Modifier.height(16.dp))

                if (recipe != null) {
                    RecipeResultCard(recipe!!)
                }

                if (error != null) {
                    Text("❌ $error", color = Color.Red)
                }
            }

            // Generate Button at bottom
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
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A))
            ) {
                if (isLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "Generate!",
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            // Bottom Navigation
            BottomNavigation()
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
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isSelected) {
                        Modifier.border(
                            width = 3.dp,
                            color = Color(0xFF8BC34A),
                            shape = RoundedCornerShape(12.dp)
                        )
                    } else {
                        Modifier
                    }
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
                    modifier = Modifier.size(50.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun BottomNavigation() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomNavItem(
            icon = R.drawable.ic_corn, // Using corn as home icon placeholder
            label = "Home",
            isSelected = false
        )
        BottomNavItem(
            icon = R.drawable.sweet_potato, // Using sweet potato as diet plan icon placeholder
            label = "Diet Plan",
            isSelected = false
        )
        BottomNavItem(
            icon = R.drawable.broccoli, // Using broccoli as dashboard icon placeholder
            label = "Dashboard",
            isSelected = false
        )
        BottomNavItem(
            icon = R.drawable.carrot, // Using carrot as leftover icon placeholder
            label = "Leftover",
            isSelected = true
        )
    }
}

@Composable
fun BottomNavItem(icon: Int, label: String, isSelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable { /* Navigation would go here */ }
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = if (isSelected) Color(0xFF8BC34A) else Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = if (isSelected) Color(0xFF8BC34A) else Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Composable
fun RecipeResultCard(recipe: Any) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            // Using reflection to get properties to maintain compatibility with original code
            val recipeName = try {
                recipe.javaClass.getDeclaredField("TranslatedRecipeName").apply { isAccessible = true }.get(recipe)?.toString() ?: "Recipe"
            } catch (e: Exception) {
                "Recipe"
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

            Text("🍽️ $recipeName", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("🌍 Cuisine: $cuisine")
            Text("🕒 Time: $time mins")
            Text("🔥 Calories: $calories")

            Spacer(modifier = Modifier.height(8.dp))

            Text("📋 Ingredients:", fontWeight = FontWeight.Bold)
            Text(ingredients)

            Spacer(modifier = Modifier.height(8.dp))

            Text("📖 Instructions:", fontWeight = FontWeight.Bold)
            Text(instructions)
        }
    }
}

// Data Class for Food Items
data class FoodItem(val name: String, val imageRes: Int)

@Preview(showBackground = true)
@Composable
fun PreviewRecipeGeneratorScreen() {
    RecipeGeneratorScreen(navController = rememberNavController())
}

// Extension function to add border to a modifier
fun Modifier.border(width: Dp, color: Color, shape: RoundedCornerShape) = this
    .padding(width / 2)
    .background(color = color, shape = shape)
    .padding(width / 2)