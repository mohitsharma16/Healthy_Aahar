package com.mohit.healthy_aahar.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mohit.healthy_aahar.R

@Composable
fun NutritionalAnalysisScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(Color(0xFFF8E8D0)) // Soft background color
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // **Back Button**
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // **Dish Image**
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_food_placeholder),
                contentDescription = "Dish Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // **Dish Name**
        Text(
            text = "Mix Salad Vegetables",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // **Nutritional Values**
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NutritionalItem("Calories", "240", Color(0xFF68A063))
            NutritionalItem("Protein", "19g", Color(0xFF5374A0))
            NutritionalItem("Carbs", "5g", Color(0xFFB65C5C))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // **Ingredients Section**
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                // **Ingredients Title**
                Text(
                    text = "Ingredients",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "6 healthy ingredients",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // **Ingredients List**
                IngredientItem(R.drawable.ic_shrimp, "Shrimp", "Briefly fried, adds instant sauce.")
                IngredientItem(R.drawable.ic_strawberry, "Strawberry", "Natural sugar from this fruit.")
                IngredientItem(R.drawable.ic_kale, "Kale", "Rich in nutrients & antioxidants.")
                IngredientItem(R.drawable.ic_corn, "Corn", "A source of fiber & energy.")
            }
        }
    }
}

// **Reusable Nutritional Item Component**
@Composable
fun NutritionalItem(name: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp, 40.dp)
                .clip(RoundedCornerShape(50))
                .background(color)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "$value $name",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

// **Reusable Ingredient Item Component**
@Composable
fun IngredientItem(imageId: Int, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = title,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

// **🔹 Nutritional Analysis Screen Preview**
@Preview(showBackground = true)
@Composable
fun NutritionalAnalysisScreenPreview() {
    NutritionalAnalysisScreen(navController = NavController(LocalContext.current))
}
