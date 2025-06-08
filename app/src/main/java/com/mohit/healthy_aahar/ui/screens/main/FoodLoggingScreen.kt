package com.mohit.healthy_aahar.ui.screens.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mohit.healthy_aahar.datastore.UserPreference
import com.mohit.healthy_aahar.ui.theme.Primary600
import com.mohit.healthy_aahar.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodLoggingScreen(navController: NavController) {
    val context = LocalContext.current
    val mainViewModel: MainViewModel = viewModel()
    val uidFlow = remember { UserPreference.getUidFlow(context) }
    val uid by uidFlow.collectAsState(initial = null)

    var breakfastText by remember { mutableStateOf("") }
    var lunchText by remember { mutableStateOf("") }
    var dinnerText by remember { mutableStateOf("") }

    val today = remember { java.time.LocalDate.now().toString() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Log Recipe",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                },
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
                    containerColor = Primary600 // Green color matching the image
                )
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .background(color = Color.White)
                .padding(horizontal = 24.dp, vertical = 16.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Text
            Text(
                text = "We see you haven't followed our recipe.\nLet us know how you cheated on us!",
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Breakfast Section
            MealInputSection(
                title = "Breakfast",
                value = breakfastText,
                onValueChange = { breakfastText = it },
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Lunch Section
            MealInputSection(
                title = "Lunch",
                value = lunchText,
                onValueChange = { lunchText = it },
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Dinner Section
            MealInputSection(
                title = "Dinner",
                value = dinnerText,
                onValueChange = { dinnerText = it },
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Save Button
            Button(
                onClick = {
                    // Log all meals that have content
                    if (uid != null) {
                        var loggedCount = 0
                        var totalToLog = 0

                        // Count how many meals have content
                        if (breakfastText.isNotBlank()) totalToLog++
                        if (lunchText.isNotBlank()) totalToLog++
                        if (dinnerText.isNotBlank()) totalToLog++

                        if (totalToLog == 0) {
                            Toast.makeText(context, "Please enter at least one meal", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        // Log breakfast if not empty
                        if (breakfastText.isNotBlank()) {
                            mainViewModel.logCustomMeal(
                                uid = uid!!,
                                date = today,
                                description = "Breakfast: ${breakfastText.trim()}"
                            )
                            loggedCount++
                        }

                        // Log lunch if not empty
                        if (lunchText.isNotBlank()) {
                            mainViewModel.logCustomMeal(
                                uid = uid!!,
                                date = today,
                                description = "Lunch: ${lunchText.trim()}"
                            )
                            loggedCount++
                        }

                        // Log dinner if not empty
                        if (dinnerText.isNotBlank()) {
                            mainViewModel.logCustomMeal(
                                uid = uid!!,
                                date = today,
                                description = "Dinner: ${dinnerText.trim()}"
                            )
                            loggedCount++
                        }

                        // Show success message and navigate back
                        Toast.makeText(
                            context,
                            "Logged $loggedCount meal(s) successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Clear all fields
                        breakfastText = ""
                        lunchText = ""
                        dinnerText = ""

                        // Navigate back after a short delay
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "User not found. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .width(100.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8BC34A)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Save",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun MealInputSection(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    "Specify the portions as well",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF8BC34A),
                unfocusedBorderColor = Color.LightGray,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray
            ),
            singleLine = true
        )
    }
}