package com.mohit.healthy_aahar.ui.screens.main

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mohit.healthy_aahar.R
import com.mohit.healthy_aahar.ui.theme.Primary600
import com.mohit.healthy_aahar.ui.viewmodel.MainViewModel
import com.mohit.healthy_aahar.model.DailyNutrition
import com.mohit.healthy_aahar.model.WeeklyReport
import com.mohit.healthy_aahar.model.NutritionReport
import com.mohit.healthy_aahar.ui.theme.Primary50
import kotlin.math.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavController,
    onMenuClick: () -> Unit,
    uid: String, // Pass the user ID
    viewModel: MainViewModel = viewModel()
) {
    var selectedPeriod by remember { mutableStateOf("Daily") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val periods = listOf("Daily", "Weekly", "Custom Range")
    val context = LocalContext.current

    // Date picker states for custom range
    var startDate by remember { mutableStateOf(LocalDate.now().minusDays(6)) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    // Observe LiveData
    val dailyNutrition by viewModel.dailyNutrition.observeAsState()
    val weeklyReport by viewModel.weeklyReport.observeAsState()
    val nutritionReport by viewModel.nutritionReport.observeAsState()
    val userDetails by viewModel.userDetails.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    // Get current date
    val currentDate = remember { LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
    val weekStart = remember {
        LocalDate.now().minusDays(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    // Default target calories - Handle nullable bmr properly
    val defaultTargetCalories = userDetails?.bmr ?: 2000.0

    // Show toast for errors instead of displaying on screen
    LaunchedEffect(error) {
        error?.let { errorMessage ->
            if (errorMessage.contains("No nutrition log found", ignoreCase = true) ||
                errorMessage.contains("no records found", ignoreCase = true) ||
                errorMessage.contains("not found", ignoreCase = true)) {
                Toast.makeText(context, "No records found for the selected period", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Load data when screen loads or period changes
    LaunchedEffect(uid, selectedPeriod, startDate, endDate) {
        viewModel.fetchUserDetails(uid) { }

        when (selectedPeriod) {
            "Daily" -> {
                viewModel.fetchDailyNutrition(uid, currentDate)
            }
            "Weekly" -> {
                viewModel.fetchWeeklyReport(uid)
                // Also fetch nutrition report for 7 days
                viewModel.fetchNutritionReport(uid, weekStart, currentDate)
            }
            "Custom Range" -> {
                val startDateStr = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val endDateStr = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                viewModel.fetchNutritionReport(uid, startDateStr, endDateStr)
            }
        }
    }

    // Date picker dialogs
    if (showStartDatePicker) {
        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                startDate = LocalDate.of(year, month + 1, dayOfMonth)
                showStartDatePicker = false
            },
            startDate.year,
            startDate.monthValue - 1,
            startDate.dayOfMonth
        )
        datePickerDialog.show()
    }

    if (showEndDatePicker) {
        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                endDate = LocalDate.of(year, month + 1, dayOfMonth)
                showEndDatePicker = false
            },
            endDate.year,
            endDate.monthValue - 1,
            endDate.dayOfMonth
        )
        datePickerDialog.show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Header
        TopAppBar(
            title = {
                Text(
                    text = "Dashboard",
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
                IconButton(onClick = { onMenuClick() }) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Primary600
            )
        )

        // Show loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary600)
            }
        }

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
                    text = "Hey, ${userDetails?.name ?: "User"}",
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
                    label = { Text("Select Period" , color = Color.Black) },
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
                    containerColor = Primary50,
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

            // Date Range Selector for Custom Range
            if (selectedPeriod == "Custom Range") {
                DateRangeSelector(
                    startDate = startDate,
                    endDate = endDate,
                    onStartDateClick = { showStartDatePicker = true },
                    onEndDateClick = { showEndDatePicker = true }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Content based on selected period
            when (selectedPeriod) {
                "Daily" -> DailyStatsUI(dailyNutrition, defaultTargetCalories)
                "Weekly" -> WeeklyStatsUI(weeklyReport, nutritionReport, defaultTargetCalories)
                "Custom Range" -> CustomRangeStatsUI(nutritionReport, defaultTargetCalories)
            }
        }
    }
}

@Composable
fun DateRangeSelector(
    startDate: LocalDate,
    endDate: LocalDate,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Select Date Range",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Start Date
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Start Date",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onStartDateClick() },
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = startDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                            modifier = Modifier.padding(12.dp),
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }

                // End Date
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "End Date",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEndDateClick() },
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = endDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                            modifier = Modifier.padding(12.dp),
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DailyStatsUI(dailyNutrition: DailyNutrition?, defaultTargetCalories: Double) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Daily Thoughts Card
        ThoughtsCard(
            title = "Today's Thoughts",
            content = "How are you feeling about your nutrition journey today? Remember, every healthy choice counts!"
        )

        // Daily Tracker with real data
        DailyTrackerCard(dailyNutrition, defaultTargetCalories)
    }
}

@Composable
fun WeeklyStatsUI(weeklyReport: WeeklyReport?, nutritionReport: NutritionReport?, defaultTargetCalories: Double) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Weekly Thoughts Card
        ThoughtsCard(
            title = "Weekly Reflection",
            content = "This week has been a journey of growth. Keep building those healthy habits one day at a time!"
        )

        // Weekly Tracker with real data
        WeeklyTrackerCard(weeklyReport, nutritionReport, defaultTargetCalories)

        // Nutrition Report Details
        if (nutritionReport != null) {
            NutritionReportCard(nutritionReport)
        }
    }
}

@Composable
fun CustomRangeStatsUI(nutritionReport: NutritionReport?, defaultTargetCalories: Double) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Custom Range Thoughts Card
        ThoughtsCard(
            title = "Your Progress",
            content = "Looking at your custom date range gives you insights into your long-term progress. Stay consistent!"
        )

        // Nutrition Report Details
        if (nutritionReport != null) {
            NutritionReportCard(nutritionReport)
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No data available for the selected date range",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun ThoughtsCard(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                    Icons.Default.Psychology,
                    contentDescription = "Thoughts",
                    tint = Color(0xFF8BC34A),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = content,
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun NutritionReportCard(nutritionReport: NutritionReport) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Nutrition Report",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "From ${nutritionReport.date_range.start} to ${nutritionReport.date_range.end}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Totals Section
            Text(
                text = "Total Consumption",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NutritionCard(
                    title = "Calories",
                    value = "${nutritionReport.totals.calories}",
                    unit = "kcal",
                    modifier = Modifier.weight(1f)
                )
                NutritionCard(
                    title = "Protein",
                    value = "${nutritionReport.totals.protein}",
                    unit = "g",
                    modifier = Modifier.weight(1f)
                )
                NutritionCard(
                    title = "Carbs",
                    value = "${nutritionReport.totals.carbs}",
                    unit = "g",
                    modifier = Modifier.weight(1f)
                )
                NutritionCard(
                    title = "Fat",
                    value = "${nutritionReport.totals.fat}",
                    unit = "g",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Averages Section
            Text(
                text = "Daily Averages",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NutritionCard(
                    title = "Calories",
                    value = "${nutritionReport.averages.calories}",
                    unit = "kcal",
                    modifier = Modifier.weight(1f)
                )
                NutritionCard(
                    title = "Protein",
                    value = "${nutritionReport.averages.protein}",
                    unit = "g",
                    modifier = Modifier.weight(1f)
                )
                NutritionCard(
                    title = "Carbs",
                    value = "${nutritionReport.averages.carbs}",
                    unit = "g",
                    modifier = Modifier.weight(1f)
                )
                NutritionCard(
                    title = "Fat",
                    value = "${nutritionReport.averages.fat}",
                    unit = "g",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Summary Section
            Text(
                text = "Summary",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Total Days: ${nutritionReport.date_range.total_days}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                text = "Days with Logs: ${nutritionReport.date_range.days_with_data}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                text = "Tracking Percentage: ${nutritionReport.summary.tracking_percentage.toInt()}%",
                fontSize = 12.sp,
                color = Color.Gray
            )

            // Show most active day if available
            nutritionReport.summary.most_active_day?.let { mostActiveDay ->
                Text(
                    text = "Most Active Day: $mostActiveDay",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
@Composable
fun DailyTrackerCard(dailyNutrition: DailyNutrition?, targetCalories: Double) {
    val actualCalories = dailyNutrition?.total?.calories ?: 0
    val actualProtein = dailyNutrition?.total?.protein ?: 0
    val actualCarbs = dailyNutrition?.total?.carbs ?: 0
    val actualFat = dailyNutrition?.total?.fat ?: 0

    // Calculate progress percentage
    val calorieProgress = if (targetCalories > 0) {
        (actualCalories / targetCalories).toFloat().coerceIn(0f, 1f)
    } else 0f
    val progressPercentage = (calorieProgress * 100).toInt()

    // Calculate target macros based on standard ratios
    val targetProtein = (targetCalories * 0.2 / 4).toInt() // 20% of calories from protein (4 cal/g)
    val targetCarbs = (targetCalories * 0.5 / 4).toInt() // 50% of calories from carbs (4 cal/g)
    val targetFat = (targetCalories * 0.3 / 9).toInt() // 30% of calories from fat (9 cal/g)

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
                        progress = calorieProgress,
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
                            text = "$progressPercentage%",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Target",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "${targetCalories.toInt()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Consumed",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "$actualCalories",
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
                    value = "$actualCalories/${targetCalories.toInt()}",
                    unit = "kcal",
                    modifier = Modifier.weight(1f)
                )
                NutritionCard(
                    title = "Protein",
                    value = "$actualProtein/$targetProtein",
                    unit = "gm",
                    modifier = Modifier.weight(1f)
                )
                NutritionCard(
                    title = "Carbs",
                    value = "$actualCarbs/$targetCarbs",
                    unit = "gm",
                    modifier = Modifier.weight(1f)
                )
                NutritionCard(
                    title = "Fat",
                    value = "$actualFat/$targetFat",
                    unit = "gm",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun WeeklyTrackerCard(weeklyReport: WeeklyReport?, nutritionReport: NutritionReport?, defaultTargetCalories: Double) {
    val totalCalories = weeklyReport?.weekly_totals?.calories ?: 0
    val targetCalories = weeklyReport?.goal_analysis?.target_daily_calories ?: defaultTargetCalories
    val weeklyTarget = (targetCalories * 7).toInt()
    val actualProtein = weeklyReport?.weekly_totals?.protein ?: 0
    val actualCarbs = weeklyReport?.weekly_totals?.carbs ?: 0
    val actualFat = weeklyReport?.weekly_totals?.fat ?: 0

    // Calculate progress percentage
    val calorieProgress = if (weeklyTarget > 0) {
        (totalCalories / weeklyTarget.toFloat()).coerceIn(0f, 1f)
    } else 0f
    val progressPercentage = (calorieProgress * 100).toInt()

    // Calculate weekly target macros
    val weeklyTargetProtein = ((targetCalories * 0.2 / 4) * 7).toInt()
    val weeklyTargetCarbs = ((targetCalories * 0.5 / 4) * 7).toInt()
    val weeklyTargetFat = ((targetCalories * 0.3 / 9) * 7).toInt()

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
                        progress = calorieProgress,
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
                            text = "$progressPercentage%",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Weekly Target",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "$weeklyTarget",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Consumed",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "$totalCalories",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Show weekly insights if available
            weeklyReport?.insights?.let { insights ->
                if (insights.isNotEmpty()) {
                    Text(
                        text = "Weekly Insights:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    insights.take(2).forEach { insight ->
                        Text(
                            text = "• $insight",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Nutrition cards row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NutritionCard(
                    title = "Calorie",
                    value = "$totalCalories/$weeklyTarget",
                    unit = "kcal",
                    modifier = Modifier.weight(1f)
                )
                NutritionCard(
                    title = "Protein",
                    value = "$actualProtein/$weeklyTargetProtein",
                    unit = "gm",
                    modifier = Modifier.weight(1f)
                )
                NutritionCard(
                    title = "Carbs",
                    value = "$actualCarbs/$weeklyTargetCarbs",
                    unit = "gm",
                    modifier = Modifier.weight(1f)
                )
                NutritionCard(
                    title = "Fat",
                    value = "$actualFat/$weeklyTargetFat",
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
        StatisticsScreen(
            navController = rememberNavController(),
            onMenuClick = {},
            uid = "test_uid"
        )
    }
}