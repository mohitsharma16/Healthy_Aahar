package com.mohit.healthy_aahar.ui.screens.auth


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.mohit.healthy_aahar.R
import com.mohit.healthy_aahar.repository.AuthRepository
import com.mohit.healthy_aahar.datastore.UserPreference
import com.mohit.healthy_aahar.ui.navigation.Screen
import com.mohit.healthy_aahar.ui.theme.Primary600

@Composable
fun SplashScreen(
    onNavigate: (String) -> Unit,
    authRepository: AuthRepository
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        // Show splash screen for at least 2 seconds
        delay(2000)

        // Check authentication and onboarding status
        val currentUser = authRepository.getCurrentUser()
        val hasSeenOnboarding = UserPreference.hasSeenOnboarding(context)

        when {
            // User is logged in -> Go directly to Home
            currentUser != null -> {
                onNavigate(Screen.Home.route)
            }
            // User has seen onboarding but not logged in -> Go to Signup
            hasSeenOnboarding -> {
                onNavigate(Screen.Signup.route)
            }
            // First time user -> Show Onboarding
            else -> {
                onNavigate(Screen.Onboarding.route)
            }
        }
    }

    // Splash Screen UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary600),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.onboarding_logo_1), // Replace with your app logo
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // App Name
            Text(
                text = "Healthy Aahar",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Your Personal Nutrition Assistant",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Loading indicator
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}