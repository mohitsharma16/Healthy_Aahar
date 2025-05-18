package com.mohit.healthy_aahar.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.mohit.healthy_aahar.R
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) } // Default to login view
    var message by remember { mutableStateOf("") }

    // Observe the auth state changes
    val authState by authViewModel.authState.collectAsState()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Light green background color
    val lightGreen = Color(0xFFE8F5E1)
    val greenButton = Color(0xFF8BC34A)
    val textGreen = Color(0xFF61A744)

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Indian flag with spices image
            Image(
                painter = painterResource(id = R.drawable.indian_flag), // Add this image to your resources
                contentDescription = "Indian flag with spices",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Welcome Back! text
            Text(
                text = "Welcome Back!",
                color = textGreen,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Email*",
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Enter your email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Password*",
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Enter your password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Login/Signup Button
            Button(
                onClick = {
                    if (isLogin) {
                        authViewModel.login(email, password)
                    } else {
                        authViewModel.signup(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                enabled = authState !is AuthState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = greenButton)
            ) {
                Text(
                    text = if (isLogin) "Login" else "Sign Up",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Switch between login and signup
            TextButton(onClick = { isLogin = !isLogin }) {
                Text(
                    text = if (isLogin) "Don't have an account? Sign Up" else "Already have an account? Login",
                    color = textGreen,
                    fontSize = 14.sp
                )
            }

            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (authState is AuthState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 16.dp),
                    color = textGreen
                )
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