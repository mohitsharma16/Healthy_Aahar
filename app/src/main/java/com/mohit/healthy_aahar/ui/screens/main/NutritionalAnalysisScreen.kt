package com.mohit.healthy_aahar.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mohit.healthy_aahar.R
import com.mohit.healthy_aahar.ui.theme.GreenBackground

@Composable
fun NutritionalAnalysisScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top header with back button and meal name
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(GreenBackground)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Scrambled eggs",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { /* Open menu */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            }
        }

        // Food image
        Image(
            painter = painterResource(id = R.drawable.ic_food_placeholder),
            contentDescription = "Food Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )

        // Nutrition information row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NutrientInfoBar(
                value = "25g",
                label = "Protein",
                barColor = Color(0xFF8EB989),
                modifier = Modifier.weight(1f)
            )

            NutrientInfoBar(
                value = "32g",
                label = "Carbs",
                barColor = Color(0xFF8EB989),
                modifier = Modifier.weight(1f)
            )

            NutrientInfoBar(
                value = "14g",
                label = "Fats",
                barColor = Color(0xFF8EB989),
                modifier = Modifier.weight(1f)
            )
        }

        // Ingredients section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Ingredients",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            IngredientsList(
                items = listOf(
                    "2 eggs",
                    "1 tbsp oil",
                    "Pinch of salt",
                    "Pinch of black pepper",
                    "Green onion"
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Instructions section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Instructions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            InstructionsList(
                items = listOf(
                    "Crack the eggs into a bowl.",
                    "Whisk them thoroughly with a fork or whisk until fully blended.",
                    "(If you like them extra soft, add 1–2 tbsp milk.)",
                    "Heat a non-stick pan over medium-low heat. Pour the eggs into the pan. Wait 10–15 seconds without touching.",
                    "Gently use a spatula, pushing eggs from edges to center."
                )
            )
        }

        // Add spacer at the bottom for the navigation bar
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun NutrientInfoBar(
    value: String,
    label: String,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = value,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .width(2.dp)
                .height(50.dp)
                .background(barColor)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun IngredientsList(items: List<String>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = "• ",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = item,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Composable
fun InstructionsList(items: List<String>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = "• ",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = item,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NutritionalAnalysisScreenPreview() {
    NutritionalAnalysisScreen(navController = NavController(LocalContext.current))
}