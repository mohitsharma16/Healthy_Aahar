package com.mohit.healthy_aahar.ui.screens.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohit.healthy_aahar.ui.theme.HealthyAaharTheme

@Composable
fun AboutUsScreen(onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8)) // Match background
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // **Header**
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "About Us",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // **Welcome Text**
        Text(
            text = "Welcome to Healthy आहार — where Indian tradition meets modern nutrition.",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // **Paragraph 1**
        Text(
            text = "At Healthy आहार, we believe that healthy eating should feel natural, personal, and rooted in our everyday lives.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // **Paragraph 2**
        Text(
            text = "Our app is designed to help you plan meals around the ingredients you already have, track your nutrition with ease, and set health goals that actually fit your lifestyle. From personalized recipe recommendations to smart, intuitive tracking, Healthy आहार blends technology with Indian culinary heritage to make your wellness journey simpler, smarter, and more joyful.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // **What makes us different?**
        Text(
            text = "What makes us different?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // **Bullet Points**
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "• Ingredient-based meal planning to reduce food waste.", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
            Text(text = "• Nutritional tracking that’s simple, visual, and empowering.", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
            Text(text = "• Culturally rich Indian recipes with modern nutrition science.", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
            Text(text = "• Smart goal-setting with real-time progress insights.", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // **Healthy आहार isn't just another app...**
        Text(
            text = "Healthy आहार isn't just another app — it's your daily partner in living better, rooted in who you are and what you love to eat.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // **Our Team**
        Text(
            text = "Our Team",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // **Team Bullet Points**
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "• Healthy आहार is built by a team of passionate students namely Sakshi Rumane, Mohit Sharma, Komal Shukla, & Madhavi Choure.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                lineHeight = 22.sp
            )
            Text(
                text = "• From Indira College of Engineering and Management, Pune, who share a common goal: to make nutrition simple, accessible, and meaningful for everyone.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                lineHeight = 22.sp
            )
            Text(
                text = "• Drawing from real Indian dietary habits, backed by research and technology, we aimed to create an app that isn't just functional — but truly feels like home.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                lineHeight = 22.sp
            )
            Text(
                text = "• Our collective backgrounds in computer science, data analytics, and nutrition studies helped us blend innovation with tradition.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                lineHeight = 22.sp
            )
            Text(
                text = "• This project is the result of months of ideation, coding, testing, and, most importantly — deep belief in the power of healthy living.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                lineHeight = 22.sp
            )
            Text(
                text = "• We are proud to present Healthy आहार as not just a project, but as a small contribution to a healthier, happier India.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                lineHeight = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // **Food is not just fuel...**
        Text(
            text = "Food is not just fuel. It’s memory, culture, and care. With Healthy आहार, we celebrate it all.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun AboutUsScreenPreview() {
    HealthyAaharTheme {
        AboutUsScreen(onBack = {})
    }
}