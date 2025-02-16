package com.mohit.healthy_aahar.ui.screens.auth

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mohit.healthy_aahar.ui.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(navController:NavController) {
    val messages = listOf(
        "Eat Healthy, Stay Fit!",
        "Personalized Meal Plans",
        "Track Your Nutrition",
        "Achieve Your Health Goals",
        "Nourish Your Body Every Day",
        "Your Health, Your Journey",
        "Healthy Eating Made Simple"
    )

    val colors = listOf(
        Color(0xFFFFC107) to Color(0xFF6D4C41),  // Yellow & Brown
        Color(0xFF4CAF50) to Color(0xFF1B5E20),  // Green & Dark Green
        Color(0xFF2196F3) to Color(0xFF0D47A1),  // Blue & Dark Blue
        Color(0xFFF44336) to Color(0xFFB71C1C),  // Red & Dark Red
        Color(0xFF9C27B0) to Color(0xFF4A148C),  // Purple & Dark Purple
        Color(0xFFFF9800) to Color(0xFFE65100),  // Orange & Dark Orange
        Color(0xFF009688) to Color(0xFF004D40)   // Teal & Dark Teal
    )

    var currentIndex by remember { mutableStateOf(0) }
    var displayedText by remember { mutableStateOf("") }
    var isDeleting by remember { mutableStateOf(false) }
    var currentColor by remember { mutableStateOf(colors[0]) }

    val animationDuration = 1500L

    // **Typing & Deleting Effect with Background Color Change on Delete**
    LaunchedEffect(currentIndex) {
        displayedText = ""
        isDeleting = false
        val fullText = messages[currentIndex]

        // **Typing Effect**
        fullText.forEach { char ->
            delay(100)
            displayedText += char
        }
        delay(1000)

        // **Start Deleting & Change Background Color**
        isDeleting = true
        currentColor = colors[(currentIndex + 1) % colors.size]

        for (i in fullText.length downTo 0) {
            delay(75)
            displayedText = displayedText.dropLast(1)
        }
        delay(500)

        isDeleting = false
        currentIndex = (currentIndex + 1) % messages.size
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDeleting) currentColor.first else colors[currentIndex].first) // Change background when deleting
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 200.dp), // Keeps text above the black section
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // **Typewriter Text Animation**
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = displayedText,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDeleting) currentColor.second else colors[currentIndex].second
                )

                // **Dot at the End**
                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(if (isDeleting) currentColor.second else colors[currentIndex].second)
                )
            }
        }

        // **Bottom Section - Login & Signup (40%) with Rounded Top Corners**
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f) // 40% of the screen
                .align(Alignment.BottomStart) // Ensures it's at the bottom
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)) // Rounded top corners
                .background(Color.Black)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // **Sign Up with Google Button**
                AuthButton(
                    text = "Sign Up with Google",
                    onClick = { /* Handle Google Sign Up */ }
                )

                // **Login Button**
                AuthButton(
                    text = "Login",
                    onClick =  {
                        navController.navigate(Screen.UserSetup.route) { // 🔹 Navigate to UserSetup
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

// **Reusable Button Composable**
@Composable
fun AuthButton(text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
        border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp)
    ) {
        Text(text, fontSize = 16.sp, color = Color.White)
    }
}
