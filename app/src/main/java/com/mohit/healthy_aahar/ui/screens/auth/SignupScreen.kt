package com.mohit.healthy_aahar.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Signup screen") }) },

    ) {
        contentPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(contentPadding).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Signup Screen", style = MaterialTheme.typography.headlineMedium)
            }

    }
}

