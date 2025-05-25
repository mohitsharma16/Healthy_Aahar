package com.mohit.healthy_aahar.ui.screens.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mohit.healthy_aahar.R
import kotlin.math.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(navController: NavController) {
    var selectedPeriod by remember { mutableStateOf("Weekly") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val periods = listOf("Daily", "Weekly")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Header
        TopAppBar(
            title = {
                Text(
                    text = "Dashboards",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* Handle menu */ }) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF8BC34A)
            )
        )

        // Scrollable Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Greeting Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Image
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE57373)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_profile),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Hey, Mohit",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Journey Section
            Text(
                text = "Your Journey..",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Period Selector Dropdown
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = selectedPeriod,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Period") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF8BC34A),
                        unfocusedBorderColor = Color.Gray
                    )
                )

                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    periods.forEach { period ->
                        DropdownMenuItem(
                            text = { Text(period) },
                            onClick = {
                                selectedPeriod = period
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content based on selected period
            when (selectedPeriod) {
                "Daily" -> DailyStatsUI()
                "Weekly" -> WeeklyStatsUI()
            }
        }
    }
}

@Composable
fun DailyStatsUI() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Daily Measurements
        WeightMeasurementsCard(
            title = "Daily Measurements",
            predicted = "77.8 Kg",
            actual = "77.8 Kg",
            isDaily = true
        )

        // Daily Tracker
        DailyTrackerCard()
    }
}

@Composable
fun WeeklyStatsUI() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Weekly Measurements
        WeightMeasurementsCard(
            title = "Weekly Measurements",
            predicted = "77.8 Kg",
            actual = "77.8 Kg",
            isDaily = false
        )

        // Weekly Tracker
        WeeklyTrackerCard()
    }
}

@Composable
fun WeightMeasurementsCard(
    title: String,
    predicted: String,
    actual: String,
    isDaily: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Icon(
                    Icons.Default.OpenInFull,
                    contentDescription = "Expand",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Column {
                    Text(
                        text = "Weight",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Predicted",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = predicted,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Actual",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = actual,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Weight chart
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                ) {
                    WeightLineChart(isDaily = isDaily)
                }
            }
        }
    }
}

@Composable
fun WeightLineChart(isDaily: Boolean) {
    val data = if (isDaily) {
        // Daily data for 24 hours
        listOf(77.8f, 77.9f, 77.7f, 77.8f, 77.9f, 78.0f, 77.8f, 77.7f, 77.8f, 77.9f, 78.0f, 77.8f)
    } else {
        // Weekly data for 7 days
        listOf(78.2f, 78.0f, 77.8f, 77.9f, 77.7f, 77.8f, 77.8f)
    }

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val maxY = data.maxOrNull() ?: 78.5f
        val minY = data.minOrNull() ?: 77.5f
        val range = maxY - minY
        val adjustedRange = if (range < 0.5f) 0.5f else range

        val spacing = size.width / (data.size - 1)
        val points = data.mapIndexed { index, value ->
            val normalizedValue = (value - minY) / adjustedRange
            Offset(
                x = index * spacing,
                y = size.height - (normalizedValue * size.height * 0.8f) - (size.height * 0.1f)
            )
        }

        // Draw line
        if (points.size > 1) {
            val path = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    val cp1 = Offset(
                        (points[i - 1].x + points[i].x) / 2,
                        points[i - 1].y
                    )
                    val cp2 = Offset(
                        (points[i - 1].x + points[i].x) / 2,
                        points[i].y
                    )
                    cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, points[i].x, points[i].y)
                }
            }

            drawPath(
                path = path,
                color = Color(0xFFFFB74D),
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Draw points
        points.forEach { point ->
            drawCircle(
                color = Color(0xFFFFB74D),
                center = point,
                radius = 4.dp.toPx()
            )
        }
    }
}

@Composable
fun DailyTrackerCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Daily Tracker",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Main Calories Circle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = 0.6f, // 60% for daily
                        modifier = Modifier.size(120.dp),
                        strokeWidth = 8.dp,
                        color = Color(0xFF8BC34A),
                        trackColor = Color(0xFFE8F5E8)
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.LocalFireDepartment,
                                contentDescription = "Calories",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Calories",
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        }
                        Text(
                            text = "60%",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Predicted",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "1,284",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Actual",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "890",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nutrition cards row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NutritionCard(
                    title = "Calorie",
                    value = "451/2050",
                    unit = "kcal",
                    modifier = Modifier.weight(1f)
                )
                NutritionCard(
                    title = "Protein",
                    value = "281/356",
                    unit = "gm",
                    modifier = Modifier.weight(1f)
                )
                NutritionCard(
                    title = "Carbs",
                    value = "45/500",
                    unit = "gm",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun WeeklyTrackerCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Weekly Tracker",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Main Calories Circle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = 0.8f, // 80% for weekly as shown in UI
                        modifier = Modifier.size(120.dp),
                        strokeWidth = 8.dp,
                        color = Color(0xFF8BC34A),
                        trackColor = Color(0xFFE8F5E8)
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.LocalFireDepartment,
                                contentDescription = "Calories",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Calories",
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        }
                        Text(
                            text = "80%",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Predicted",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "1,284",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Actual",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "890",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nutrition cards row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NutritionCard(
                    title = "Calorie",
                    value = "451/2050",
                    unit = "kcal",
                    modifier = Modifier.weight(1f)
                )
                NutritionCard(
                    title = "Protein",
                    value = "281/356",
                    unit = "gm",
                    modifier = Modifier.weight(1f)
                )
                NutritionCard(
                    title = "Carbs",
                    value = "45/500",
                    unit = "gm",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun NutritionCard(
    title: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(80.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Circle,
                    contentDescription = null,
                    tint = Color(0xFF8BC34A),
                    modifier = Modifier.size(8.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    fontSize = 10.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = unit,
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StatisticsScreenPreview() {
    MaterialTheme {
        StatisticsScreen(navController = rememberNavController())
    }
}