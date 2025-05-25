// === File: AppDrawerContent.kt ===
package com.mohit.healthy_aahar.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohit.healthy_aahar.R
import com.mohit.healthy_aahar.ui.navigation.Screen

@Composable
fun AppDrawerContent(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // User Info Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Mohit", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Edit", fontSize = 14.sp, color = Color(0xFF4CAF50))
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            DrawerItem(
                Icons.Outlined.Info,
                "About Us",
                onClick = { onNavigate(Screen.AboutUs.route) }
            )
            DrawerItem(
                Icons.Outlined.Feedback,
                "Feedback",
                onClick = { onNavigate(Screen.Feedback.route) }
            )
            DrawerItem(
                Icons.Outlined.Restaurant,
                "Recipes",
                onClick = { onNavigate(Screen.RecipeGenerator.route) }
            )
            DrawerItem(
                Icons.Outlined.Receipt,
                "Log Recipes",
                onClick = { onNavigate(Screen.FoodLogging.route) }
            )

        }

        DrawerItem(
            icon = Icons.Outlined.ExitToApp,
            title = "Logout",
            onClick = { onNavigate("logout") },
            tint = Color(0xFF4CAF50)
        )
    }
}

@Composable
fun DrawerItem(icon: ImageVector, title: String, onClick: () -> Unit, tint: Color = Color.Gray) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp, color = Color.Black)
    }
}
