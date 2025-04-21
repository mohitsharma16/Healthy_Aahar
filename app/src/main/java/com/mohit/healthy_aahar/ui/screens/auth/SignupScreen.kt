package com.mohit.healthy_aahar.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mohit.healthy_aahar.ui.navigation.Screen
import com.mohit.healthy_aahar.ui.viewmodel.AuthState
import com.mohit.healthy_aahar.ui.viewmodel.AuthViewModel
import com.mohit.healthy_aahar.datastore.UserPreference
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    // Observe the auth state changes
    val authState by authViewModel.authState.collectAsState()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Handle navigation & messages based on auth state
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                val isSignup = (authState as AuthState.Success).isSignup
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    lifecycleOwner.lifecycleScope.launch {
                        UserPreference.saveUid(context, uid)
                    }
                } else {
                    Toast.makeText(context, "UID not found", Toast.LENGTH_SHORT).show()
                }
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
        topBar = {
            TopAppBar(
                title = { Text(if (isLogin) "Login Screen" else "Signup Screen") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp)
                .background(Color(0xFFF0F0F0)), // Neutral background
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Center the content
        ) {
            Text(
                text = if (isLogin) "Login" else "Sign Up",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.elevatedCardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(Icons.Filled.Mail, contentDescription = "Email Icon")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(Icons.Filled.Lock, contentDescription = "Password Icon")
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isLogin) {
                        authViewModel.login(email, password)
                    } else {
                        authViewModel.signup(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = authState !is AuthState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = if (isLogin) "Login" else "Sign Up",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { isLogin = !isLogin }) {
                Text(
                    text = if (isLogin) "Don't have an account? Sign Up" else "Already have an account? Login",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary)
                )
            }

            if (message.isNotEmpty()) {
                Text(text = message, color = MaterialTheme.colorScheme.error)
            }

            if (authState is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignupScreen() {
    // You can replace this with a mock view model if needed
    SignupScreen(navController = NavController(LocalContext.current))
}
