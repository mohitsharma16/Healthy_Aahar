package com.mohit.healthy_aahar.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealHistoryScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Meal History") }) }
    ) {
        contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Meal History Plan", style = MaterialTheme.typography.headlineMedium)
        }
    }
}