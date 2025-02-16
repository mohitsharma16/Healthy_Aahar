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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohit.healthy_aahar.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    var pageIndex by remember { mutableStateOf(0) }

    val pages = listOf(
        OnboardingPage(
            title = "WELCOME",
            centerText = "to",
            highlightedText = "Healthy Aahar",
//            description = "Your Guide to Nutritious Living!",
            image = R.drawable.onboarding_1, // Add correct image in drawable
            description = "Your Guide to Nutritious Living!",
            bgColor = Color(0xFFE9F5C7)
        ),
        OnboardingPage(
            title = "Discover personalized",
            centerText = "",
            highlightedText = "",
            description = "meal plans, nutrition tips, and diet tracking to achieve your health goals",
            image = R.drawable.onboarding_2,
            bgColor = Color(0xFFD2EAE2)
        ),
        OnboardingPage(
            title = "How it Works",
            centerText = "",
            highlightedText = "",
            description = """
                ⭐ Step 1: Tell us about your dietary preferences and health goals.
                ⭐ Step 2: Get a personalized meal plan based on your needs.
                ⭐ Step 3: Track your nutrition and stay on top of your diet.
            """.trimIndent(),
            image = null,
            bgColor = Color(0xFFFCEEC8)
        )
    )

    val currentPage = pages[pageIndex]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(currentPage.bgColor)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // **Header (App Name & Arrow Button)**
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Healthy Aahar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF905E28)
                )
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable {
                            if (pageIndex < pages.size - 1) {
                                pageIndex++
                            } else {
                                onComplete()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Next",
                        tint = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // **Main Onboarding Card (Covers Full Page After Header)**
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    fadeIn() + slideInHorizontally { it } with fadeOut() + slideOutHorizontally { -it }
                },
                label = "Onboarding Transition"
            ) { page ->
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight() // **Full-Screen Card**
                        .padding(horizontal = 8.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // **Title, Center Text, and Highlighted Text (First Screen)**
                        if (pageIndex == 0) {
                            Text(
                                text = page.title,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = page.centerText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = page.highlightedText,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5C8124)
                            )
                        } else {
                            // **Title for Other Screens**
                            Text(
                                text = page.title,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // **Image (If Available)**
                        if (page.image != null) {
                            Image(
                                painter = painterResource(id = page.image),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // **Description Text**
                        Text(
                            text = page.description,
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // **Get Started Button on Last Screen**
                        if (pageIndex == pages.size - 1) {
                            Button(
                                onClick = onComplete,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCEEC8)),
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                Text(text = "Get Started", fontSize = 16.sp, color = Color.Black)
                            }
                        }

                        // **Pagination Indicator**
                        Row(
                            modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            pages.indices.forEach { index ->
                                Box(
                                    modifier = Modifier
                                        .size(if (index == pageIndex) 8.dp else 6.dp)
                                        .padding(4.dp)
                                        .clip(CircleShape)
                                        .background(if (index == pageIndex) Color(0xFFFCCB67) else Color.LightGray)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// **Data Class for Onboarding Pages**
data class OnboardingPage(
    val title: String,
    val centerText: String,
    val highlightedText: String,
    val description: String,
    val image: Int?,
    val bgColor: Color
)
