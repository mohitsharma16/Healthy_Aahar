package com.mohit.healthy_aahar.ui.screens.auth

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohit.healthy_aahar.R
import com.mohit.healthy_aahar.datastore.UserPreference
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    var pageIndex by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val lightGreen = Color(0xFFE8F5E1) // Background color from images
    val textGreen = Color(0xFF61A744) // Text green color from images
    val darkBrown = Color(0xFF48341C) // Dark text color

    val pages = listOf(
        OnboardingPage(
            title = "Welcome to Healthy आहार",
            description = "where Indian flavors meet your fitness goals. Let's spice up your health journey!",
            image = R.drawable.onboarding_logo_1, // Image 4
            bgColor = lightGreen,
            textColor = textGreen
        ),
        OnboardingPage(
            title = "Eat Healthy, Stay Rooted",
            description = "Discover personalized diet plans tailored to your Indian taste and lifestyle.",
            image = R.drawable.onboarding_health, // Image 3
            bgColor = lightGreen,
            textColor = textGreen
        ),
        OnboardingPage(
            title = "Your Ingredients, Your Recipes",
            description = "Tell us what's in your kitchen, and get nutritious recipes instantly — minimizing waste, maximizing flavor.",
            image = R.drawable.onboarding_ingrident, // Image 2
            bgColor = lightGreen,
            textColor = textGreen
        ),
        OnboardingPage(
            title = "Track Your Health Journey",
            description = "Set goals, monitor progress, and receive smart reminders — all in one place.",
            image = R.drawable.onboarding_fit, // Image 1
            bgColor = lightGreen,
            textColor = textGreen
        )
    )

    val currentPage = pages[pageIndex]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(currentPage.bgColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Main Content - Image and Text
            AnimatedContent(
                targetState = pageIndex,
                transitionSpec = {
                    fadeIn() + slideInHorizontally { it } with fadeOut() + slideOutHorizontally { -it }
                },
                label = "Onboarding Transition"
            ) { index ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Image
                    Image(
                        painter = painterResource(id = pages[index].image),
                        contentDescription = null,
                        modifier = Modifier
                            .size(240.dp)
                            .padding(bottom = 40.dp)
                    )

                    // Title
                    Text(
                        text = pages[index].title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = darkBrown,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    Text(
                        text = pages[index].description,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Bottom Navigation Area
            Column(
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Pagination Dots
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    pages.indices.forEach { index ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index == pageIndex) textGreen else Color.LightGray.copy(alpha = 0.5f)
                                )
                        )
                    }
                }

                // Next Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable {
                            if (pageIndex < pages.size - 1) {
                                pageIndex++
                            } else {
                                // User has completed onboarding
                                scope.launch {
                                    // Mark onboarding as completed
                                    UserPreference.setOnboardingCompleted(context)
                                    // Then proceed with existing logic
                                    onComplete()
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Next",
                        tint = darkBrown
                    )
                }
            }
        }
    }
}

// Updated Data Class for Onboarding Pages
data class OnboardingPage(
    val title: String,
    val description: String,
    val image: Int,
    val bgColor: Color,
    val textColor: Color
)