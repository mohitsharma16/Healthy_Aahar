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
fun FoodLoggingScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Food Logging") }) }
    ) {
        contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Food Logging Plan", style = MaterialTheme.typography.headlineMedium)
        }
    }
}
