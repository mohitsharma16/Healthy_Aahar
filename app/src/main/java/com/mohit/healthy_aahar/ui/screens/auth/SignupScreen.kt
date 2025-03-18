package com.mohit.healthy_aahar.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mohit.healthy_aahar.ui.navigation.Screen
import com.mohit.healthy_aahar.ui.viewmodel.AuthState
import com.mohit.healthy_aahar.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    // Observe the auth state changes
    val authState by authViewModel.authState.collectAsState()

    // Handle navigation & messages based on auth state
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                val isSignup = (authState as AuthState.Success).isSignup
                if (isSignup) {
                    navController.navigate(Screen.UserSetup.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
                authViewModel.resetAuthState() // Reset state after navigation
            }

            is AuthState.Error -> {
                message = (authState as AuthState.Error).message
            }

            AuthState.Loading -> {
                message = "Loading..."
            }

            AuthState.Idle -> Unit
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(if (isLogin) "Login Screen" else "Signup Screen") }) },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isLogin) "Login" else "Sign Up",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isLogin) {
                        authViewModel.login(email, password)
                    } else {
                        authViewModel.signup(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = authState !is AuthState.Loading
            ) {
                Text(text = if (isLogin) "Login" else "Sign Up")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { isLogin = !isLogin }) {
                Text(text = if (isLogin) "Don't have an account? Sign Up" else "Already have an account? Login")
            }

            if (message.isNotEmpty()) {
                Text(text = message, color = MaterialTheme.colorScheme.error)
            }

            if (authState is AuthState.Loading) {
                CircularProgressIndicator()
            }
        }
    }
}
