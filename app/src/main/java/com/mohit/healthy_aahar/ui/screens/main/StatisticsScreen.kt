package com.mohit.healthy_aahar.ui.screens.main

import android.graphics.Color as AndroidColor
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import kotlin.math.roundToInt

//@Composable
//fun StatisticsScreen() {
//    val context = LocalContext.current
//
//    // Sample Data (Replace with real data from Firebase/DB)
//    val totalCalories = 2500f  // Daily Goal
//    val consumedCalories = 1800f // User Consumed
//    val carbs = 250f
//    val proteins = 100f
//    val fats = 80f
//
//    val carbsGoal = 300f
//    val proteinsGoal = 120f
//    val fatsGoal = 90f
//
//    val carbsProgress = carbs / carbsGoal
//    val proteinsProgress = proteins / proteinsGoal
//    val fatsProgress = fats / fatsGoal
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .background(Color(0xFFF8F8F8)), // Light Background
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Statistics",
//            fontSize = 28.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFF212121)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Macronutrient Progress Indicators
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            MacroProgress("Carbs", carbs, carbsGoal, carbsProgress, Color(0xFFFF9800))
//            MacroProgress("Proteins", proteins, proteinsGoal, proteinsProgress, Color(0xFF4CAF50))
//            MacroProgress("Fats", fats, fatsGoal, fatsProgress, Color(0xFFF44336))
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Calorie Intake Chart
//        Text(
//            text = "Daily Calorie Intake",
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFF424242)
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//
//        AndroidView(
//            factory = { context ->
//                BarChart(context).apply {
//                    description = Description().apply { text = "" }
//                    setDrawGridBackground(false)
//                    legend.isEnabled = false
//                    setScaleEnabled(false)
//
//                    axisLeft.apply {
//                        textSize = 12f
//                        setDrawGridLines(false)
//                        axisMinimum = 0f
//                        axisMaximum = totalCalories
//                        textColor = AndroidColor.DKGRAY
//                    }
//                    axisRight.isEnabled = false
//                    xAxis.apply {
//                        textSize = 12f
//                        setDrawGridLines(false)
//                        setDrawAxisLine(true)
//                        granularity = 1f
//                        textColor = AndroidColor.DKGRAY
//                        position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
//                    }
//
//                    val entries = listOf(
//                        BarEntry(1f, consumedCalories),
//                        BarEntry(2f, totalCalories)
//                    )
//
//                    val barDataSet = BarDataSet(entries, "Calories")
//                    barDataSet.colors = listOf(
//                        AndroidColor.parseColor("#4CAF50"), // Green for consumed
//                        AndroidColor.parseColor("#FFC107")  // Yellow for total
//                    )
//
//                    val barData = BarData(barDataSet)
//                    barData.barWidth = 0.4f
//                    data = barData
//                    invalidate()
//                }
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(250.dp)
//        )
//    }
//}
//
//// Reusable Circular Progress Indicator
//@Composable
//fun MacroProgress(name: String, value: Float, goal: Float, progress: Float, color: Color) {
//    val animatedProgress = animateFloatAsState(
//        targetValue = progress,
//        animationSpec = tween(durationMillis = 1000)
//    )
//
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Box(
//            modifier = Modifier
//                .size(100.dp)
//                .clip(CircleShape)
//                .background(Color.White),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator(
//                progress = animatedProgress.value,
//                color = color,
//                strokeWidth = 8.dp
//            )
//            Text(
//                text = "${((value / goal) * 100).roundToInt()}%",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold
//            )
//        }
//        Spacer(modifier = Modifier.height(4.dp))
//        Text(name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = color)
//    }
//}

@Composable
fun StatisticsScreen(navController : NavController) {
    var selectedTab by remember { mutableStateOf("Water Intake") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("Statistics", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        // Tabs for Water & Calories Intake
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TabButton("Water Intake", selectedTab) { selectedTab = it }
            TabButton("Calories Intake", selectedTab) { selectedTab = it }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            "Water Intake" -> WaterIntakeStats()
            "Calories Intake" -> CaloriesIntakeStats()
        }
    }
}

// **Tab Button Component**
@Composable
fun TabButton(tabName: String, selectedTab: String, onSelect: (String) -> Unit) {
    TextButton(
        onClick = { onSelect(tabName) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (tabName == selectedTab) Color.Blue else Color.LightGray
        )
    ) {
        Text(tabName, color = Color.White, fontSize = 16.sp)
    }
}

// **Water Intake Stats UI**
@Composable
fun WaterIntakeStats() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Weekly Percentage Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.Blue, RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Weekly Percentage\n0.0%", color = Color.White, fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weekly Water Completion Graph
        Text("Weekly Water Completion", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        WaterCompletionGraph()
    }
}

// **Calories Intake Stats UI**
@Composable
fun CaloriesIntakeStats() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Daily Calories Progress
        Text("Daily Calories Intake", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        // Circular Progress Indicators for Macro Nutrients
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MacroNutrientProgress("Carbs", 0.7f, Color.Green)
            MacroNutrientProgress("Protein", 0.5f, Color.Red)
            MacroNutrientProgress("Fats", 0.3f, Color.Yellow)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Calories Intake Pie Chart
        Text("Caloric Breakdown", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        CaloriesIntakeGraph()
    }
}

// **Macro Nutrient Circular Progress**
@Composable
fun MacroNutrientProgress(label: String, progress: Float, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(progress = progress, color = color)
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

// **Water Completion Graph**
@Composable
fun WaterCompletionGraph() {
    AndroidView(factory = { context ->
        BarChart(context).apply {
            val entries = listOf(
                BarEntry(1f, 2f),
                BarEntry(2f, 3f),
                BarEntry(3f, 1f),
                BarEntry(4f, 4f),
                BarEntry(5f, 2f),
                BarEntry(6f, 3f),
                BarEntry(7f, 5f)
            )

            val dataSet = BarDataSet(entries, "Water Intake (Liters)").apply {
                colors = ColorTemplate.MATERIAL_COLORS.toList()
            }

            data = BarData(dataSet)
            description.isEnabled = false
            animateY(1000)
        }
    }, modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
    )
}

// **Caloric Breakdown Pie Chart**
@Composable
fun CaloriesIntakeGraph() {
    AndroidView(factory = { context ->
        PieChart(context).apply {
            val entries = listOf(
                PieEntry(50f, "Carbs"),
                PieEntry(30f, "Protein"),
                PieEntry(20f, "Fats")
            )

            val dataSet = PieDataSet(entries, "").apply {
                colors = listOf(AndroidColor.GREEN, AndroidColor.RED, AndroidColor.YELLOW)
            }

            data = PieData(dataSet)
            description.isEnabled = false
            isDrawHoleEnabled = true
            setEntryLabelColor(AndroidColor.BLACK)
            animateY(1000)
        }
    }, modifier = Modifier
        .fillMaxWidth()
        .height(250.dp)
    )
}


@Preview (showBackground = true)
@Composable
fun PreviewStatisticsScreen() {
    StatisticsScreen(navController = NavController(LocalContext.current))
}
