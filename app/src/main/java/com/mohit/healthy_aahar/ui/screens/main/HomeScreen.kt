package com.mohit.healthy_aahar.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mohit.healthy_aahar.R
import com.mohit.healthy_aahar.ui.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Healthy Aahar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        // **Top Section: User Profile + Notification**
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_profile), // Replace with actual user image
                contentDescription = "User Profile",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            IconButton(onClick = { /* Navigate to Notifications */ }) {
                Icon(Icons.Outlined.Notifications, contentDescription = "Notifications", Modifier.size(36.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // **User Profile Section**
        Row(verticalAlignment = Alignment.CenterVertically) {

//            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Hello,", fontSize = 20.sp, color = Color.Gray)
            Text(text = "Kaluna", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        }

        Spacer(modifier = Modifier.height(8.dp))

        // **Main Heading: Complete Your Daily Nutrition**
        Text(
            text = "Complete your daily Nutrition",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // **Horizontally Scrollable Nutrition Cards**
        HorizontalNutritionSection()

        Spacer(modifier = Modifier.height(16.dp))

        // **Meal Selector Tabs**
        MealSelector()

        Spacer(modifier = Modifier.height(16.dp))

        // **Highlighted Meal Card**
        MealCard(navController)
    }
}

// **Composable for Horizontally Scrollable Nutrition Cards**
@Composable
fun HorizontalNutritionSection() {
    val nutritionData = listOf(
        NutritionItem("Kalori", "832kCal", R.drawable.ic_fire, Color(0xFFFFFFFF)),  // White
        NutritionItem("Protein", "200gr", R.drawable.ic_protein, Color(0xFFDDECA8)),  // Light Green
        NutritionItem("Water", "1000ml", R.drawable.ic_water, Color(0xFFFCE8D5))   // Light Peach
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(nutritionData) { item ->
            NutritionCard(title = item.title, value = item.value, icon = item.icon, bgColor = item.color)
        }
    }
}

// **Data Class for Nutrition Items**
data class NutritionItem(val title: String, val value: String, val icon: Int, val color: Color)

// **Composable for a Single Nutrition Card**
@Composable
fun NutritionCard(title: String, value: String, icon: Int, bgColor: Color) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .width(120.dp)
            .height(140.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // **Top Section: Title + Icon**
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    modifier = Modifier.size(18.dp),
//                  //tint = Color.Black
                )
            }

            // **Middle Section: "Total Cons" Label**
            Text(
                text = "Total cons",
                fontSize = 12.sp,
                color = Color.Gray
            )

            // **Bottom Section: Value**
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}


// **Composable for Meal Selector Tabs**
@Composable
fun MealSelector() {
    var selectedMeal by remember { mutableStateOf("Breakfast") }
    val meals = listOf("Breakfast", "Lunch", "Dinner")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        meals.forEach { meal ->
            Button(
                onClick = { selectedMeal = meal },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedMeal == meal) Color.Black else Color.LightGray
                )
            ) {
                Text(meal, color = if (selectedMeal == meal) Color.White else Color.Black)
            }
        }
    }
}

// **Composable for Meal Card**
// **Composable for Meal Card with Navigation**
@Composable
fun MealCard(navController: NavController) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                navController.navigate(Screen.NutritionalAnalysis.route) // 🔹 Navigates to Nutritional Analysis
            }
    ) {
        Column {
            // **Meal Image with Overlay & Favorite Button**
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_food_placeholder), // Replace with actual meal image
                    contentDescription = "Meal Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                )

                // **Gradient Overlay**
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )

                // **Heart Favorite Button**
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = { /* Handle favorite action */ },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White, shape = CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_heart),
                            contentDescription = "Favorite",
                            tint = Color.Red
                        )
                    }
                }

                // **Meal Details (Title & Description)**
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Meat rice with sauce",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Fresh and low-calorie",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // **Nutrition Information**
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutritionInfo("290", "Calories", Color(0xFFDDECA8)) // Light Green
                NutritionInfo("16g", "Protein", Color(0xFFD1D5E1))  // Light Gray-Blue
                NutritionInfo("56g", "Carbs", Color(0xFFEEC8A8))   // Light Orange
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
// **Composable for Nutrition Info Items with Colored Line**
@Composable
fun NutritionInfo(value: String, label: String, lineColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(color = lineColor, shape = RoundedCornerShape(2.dp))
        )
    }
}


// **Preview Function**
@Preview(showBackground = true)
@Composable
fun PreviewFullHomeScreen() {
    HomeScreen(navController = rememberNavController())
}
