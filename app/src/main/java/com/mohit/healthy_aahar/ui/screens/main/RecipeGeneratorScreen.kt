package com.mohit.healthy_aahar.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
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
    var typedIngredient by remember { mutableStateOf("") }
    val recipe by viewModel.generatedRecipe.observeAsState()
    val error by viewModel.error.observeAsState()
    val isLoading = remember { mutableStateOf(false) }

    val foodItems = listOf(
        FoodItem("Cabbage", R.drawable.cabbage),
        FoodItem("Chicken", R.drawable.chicken),
        FoodItem("Meat", R.drawable.meat),
        FoodItem("Egg", R.drawable.egg),
        FoodItem("Broccoli", R.drawable.broccoli),
        FoodItem("Corn", R.drawable.ic_corn),
        FoodItem("Carrot", R.drawable.carrot),
        FoodItem("Sweet Potato", R.drawable.sweet_potato),
        FoodItem("Lettuce", R.drawable.lettuce)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe Generator", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔍 Search Box
            OutlinedTextField(
                value = typedIngredient,
                onValueChange = { typedIngredient = it },
                label = { Text("Add an ingredient") },
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val item = typedIngredient.trim().lowercase()
                    if (item.isNotEmpty()) {
                        selectedItems = selectedItems + item.replaceFirstChar { it.uppercase() }
                        typedIngredient = ""
                    }
                },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Add Ingredient", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Or tap from below", fontWeight = FontWeight.Medium, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(12.dp))

            // 🍴 Ingredient cards
            Column {
                val chunkedItems = foodItems.chunked(3)
                chunkedItems.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowItems.forEach { item ->
                            val isSelected = selectedItems.contains(item.name)
                            FoodItemCard(item, isSelected) {
                                selectedItems = if (isSelected) {
                                    selectedItems - item.name
                                } else {
                                    selectedItems + item.name
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🍳 Generate Button
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
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Generate Recipe", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🌀 Loading
            if (isLoading.value) {
                CircularProgressIndicator()
            }

            // 🧾 Recipe Card
            recipe?.let {
                isLoading.value = false
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("🍽️ ${it.TranslatedRecipeName}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("🌍 Cuisine: ${it.Cuisine}")
                        Text("🕒 Time: ${it.TotalTimeInMins} mins")
                        Text("🔥 Calories: ${it.Calories}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("📋 Ingredients:", fontWeight = FontWeight.Bold)
                        Text(it.TranslatedIngredients.orEmpty())
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("📖 Instructions:", fontWeight = FontWeight.Bold)
                        Text(it.TranslatedInstructions.orEmpty())
                        Spacer(modifier = Modifier.height(8.dp))

                    }
                }
            }

            // ❌ Error
            error?.let {
                isLoading.value = false
                Text("❌ $it", color = Color.Red)
            }
        }
    }
}


// *Data Class for Food Items*
data class FoodItem(val name: String, val imageRes: Int)

@Composable
fun FoodItemCard(item: FoodItem, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFDFFFD6) else Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// 🔥 *PREVIEW FUNCTION*
@Preview(showBackground = true)
@Composable
fun PreviewFoodSelectionScreen() {
    RecipeGeneratorScreen(navController = rememberNavController())
}