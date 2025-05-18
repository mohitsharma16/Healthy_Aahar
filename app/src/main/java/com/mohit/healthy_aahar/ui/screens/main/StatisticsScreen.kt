package com.mohit.healthy_aahar.ui.screens.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.EmojiFoodBeverage
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlin.math.max

@Composable
fun StatisticsScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Daily", "Weekly")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(16.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clip(RoundedCornerShape(16.dp))
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            0 -> DailyStatsUI()
            1 -> WeeklyStatsUI()
        }
    }
}

@Composable
fun DailyStatsUI() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Today's Overview", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        StatCard("Calories", 1400, 2000, Color(0xFFE91E63), Icons.Default.Whatshot)
        StatCard("Protein", 60, 100, Color(0xFF2196F3), Icons.Default.FitnessCenter)
        StatCard("Carbs", 180, 300, Color(0xFF4CAF50), Icons.Default.BubbleChart)
        StatCard("Fat", 50, 70, Color(0xFFFF9800), Icons.Default.EmojiFoodBeverage)
    }
}

@Composable
fun StatCard(title: String, value: Int, goal: Int, color: Color, icon: ImageVector) {
    val progress = value / goal.toFloat()

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(40.dp))

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("$value / $goal", fontSize = 14.sp, color = Color.Gray)
                LinearProgressIndicator(
                    progress = progress.coerceIn(0f, 1f),
                    color = color,
                    trackColor = color.copy(alpha = 0.2f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@Composable
fun WeeklyStatsUI() {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Text("Weekly Trends", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        WeeklyLineChart(
            title = "Calories",
            data = listOf(1800, 1900, 1700, 2000, 2100, 1600, 1850),
            color = Color(0xFFE91E63)
        )

        WeeklyLineChart(
            title = "Protein",
            data = listOf(65, 70, 60, 80, 75, 55, 68),
            color = Color(0xFF2196F3)
        )

        WeeklyLineChart(
            title = "Carbs",
            data = listOf(200, 220, 180, 250, 240, 190, 210),
            color = Color(0xFF4CAF50)
        )

        WeeklyLineChart(
            title = "Fat",
            data = listOf(50, 55, 48, 60, 58, 45, 52),
            color = Color(0xFFFF9800)
        )
    }
}

@Composable
fun WeeklyLineChart(title: String, data: List<Int>, color: Color) {
    val maxY = (data.maxOrNull() ?: 1).toFloat()

    Column {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(8.dp)) {

            val spacing = size.width / (data.size - 1)
            val points = data.mapIndexed { index, value ->
                Offset(x = index * spacing, y = size.height - (value / maxY) * size.height)
            }

            // Draw line
            drawPath(
                path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    for (point in points.drop(1)) {
                        lineTo(point.x, point.y)
                    }
                },
                color = color,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )

            // Draw points
            points.forEach {
                drawCircle(
                    color = color,
                    center = it,
                    radius = 6.dp.toPx()
                )
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StatisticsScreenPreview() {
    MaterialTheme {
        StatisticsScreen(navController= rememberNavController() )
    }
}
