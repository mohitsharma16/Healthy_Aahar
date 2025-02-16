package com.mohit.healthy_aahar.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mohit.healthy_aahar.R
import com.mohit.healthy_aahar.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeGeneratorScreen(navController: NavController) {
    var selectedItems by remember { mutableStateOf(setOf<String>()) }

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
                title = { Text("Let's Start..", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            Text(
                text = "What material you like the most",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "You can choose more than 1 answer",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* Handle Next Button Click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Next", fontSize = 18.sp, color = Color.White)
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