package com.mohit.healthy_aahar.ui.screens.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.mohit.healthy_aahar.model.Meal
import com.mohit.healthy_aahar.ui.navigation.Screen
import com.mohit.healthy_aahar.ui.viewmodel.MainViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mohit.healthy_aahar.datastore.UserPreference
import com.mohit.healthy_aahar.model.LogMealRequest
import com.mohit.healthy_aahar.model.UserDetails
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietPlanScreen(navController: NavController, onMenuClick: () -> Unit) {
    val mainViewModel: MainViewModel = viewModel()
    val mealPlanState by mainViewModel.mealPlan.observeAsState()
    val userDetailsState by mainViewModel.userDetails.observeAsState() // Add this state
    val context = LocalContext.current
    val uidFlow = remember { UserPreference.getUidFlow(context) }
    val uid by uidFlow.collectAsState(initial = null)

    // Trigger once on first composition
    LaunchedEffect(uid) {
        uid?.let {
            mainViewModel.getMealPlan(it)
            mainViewModel.fetchUserDetails(it) { name ->
                // Optional: Handle the callback if needed
                Log.d("DietPlan", "Fetched user: $name")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    "Diet Plan",
                    color = Color.White,
                    fontSize = 20.sp,
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
                IconButton(onClick = { onMenuClick()}) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF8FBC8F)
            )
        )

        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Details Card with dynamic data
            UserDetailsCard(userDetails = userDetailsState)

            // Recommended Meals Section
            if (mealPlanState?.meal_plan?.isNotEmpty() == true) {
                RecommendedMealsSection(
                    meals = mealPlanState?.meal_plan ?: emptyList(),
                    viewModel = mainViewModel,
                    uid = uid
                )
            }
        }
    }
}

@Composable
fun UserDetailsCard(userDetails: UserDetails?) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF8FBC8F))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "User details",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (userDetails == null) {
                // Skeleton loading state
                UserDetailsSkeletonLoader()
            } else {
                // Actual data
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        UserDetailItem("Name:", userDetails.name)
                        Spacer(modifier = Modifier.height(8.dp))
                        UserDetailItem("Age:", "${userDetails.age} years")
                        Spacer(modifier = Modifier.height(8.dp))
                        UserDetailItem("Weight:", "${userDetails.weight} kg")
                        Spacer(modifier = Modifier.height(8.dp))
                        UserDetailItem("Goal:", userDetails.goal.replaceFirstChar { it.uppercase() })
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        UserDetailItem("Gender:", userDetails.gender.replaceFirstChar { it.uppercase() })
                        Spacer(modifier = Modifier.height(8.dp))
                        UserDetailItem("Height:", "${userDetails.height} cm")
                        Spacer(modifier = Modifier.height(8.dp))
                        UserDetailItem("Activity Level:", userDetails.activity_level.replaceFirstChar { it.uppercase() })
                        Spacer(modifier = Modifier.height(8.dp))
                        UserDetailItem("BMR:", "${userDetails.bmr?.toInt()} kcal")
                    }
                }
            }
        }
    }
}

@Composable
fun UserDetailsSkeletonLoader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            repeat(4) {
                SkeletonDetailItem()
                if (it < 3) Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            repeat(4) {
                SkeletonDetailItem()
                if (it < 3) Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SkeletonDetailItem() {
    Column {
        // Skeleton for label
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(14.dp)
                .background(
                    Color.Gray.copy(alpha = 0.3f),
                    RoundedCornerShape(4.dp)
                )
        )
        Spacer(modifier = Modifier.height(4.dp))
        // Skeleton for value
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(14.dp)
                .background(
                    Color.Gray.copy(alpha = 0.2f),
                    RoundedCornerShape(4.dp)
                )
        )
    }
}

@Composable
fun UserDetailItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun RecommendedMealsSection(
    meals: List<Meal>,
    viewModel: MainViewModel,
    uid: String?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF8FBC8F))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Recommended Meals",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            meals.forEach { meal ->
                MealCard(meal = meal, viewModel = viewModel, uid = uid)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealCard(meal: Meal, viewModel: MainViewModel, uid: String?) {
    val context = LocalContext.current
    val today = remember { java.time.LocalDate.now().toString() }
    var selectedMealType by remember { mutableStateOf("Breakfast") }
    var showDropdown by remember { mutableStateOf(false) }
    var isLogging by remember { mutableStateOf(false) }

    val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Snack")
    val isMealLogged = meal.isLogged == true

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF6EC))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                meal.TranslatedRecipeName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                "Cuisine: ${meal.Cuisine}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Text(
                "Time: ${meal.TotalTimeInMins} mins",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "🔥 ${meal.Calories} kcal | 🥩 ${meal.Protein}g | 🧈 ${meal.Fat}g | 🍞 ${meal.Carbs}g",
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            meal.TranslatedInstructions?.let { instructions ->
                Text(
                    instructions,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Meal Type Dropdown
            Column {
                Text(
                    "Select Meal Type:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isMealLogged) Color.Gray else Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = showDropdown,
                    onExpandedChange = {
                        if (!isMealLogged) {
                            showDropdown = !showDropdown
                        }
                    }
                ) {
                    OutlinedTextField(
                        value = selectedMealType,
                        onValueChange = { },
                        readOnly = true,
                        enabled = !isMealLogged,
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = if (isMealLogged) Color.Gray else Color.Black
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF8FBC8F),
                            unfocusedBorderColor = Color.Gray,
                            disabledBorderColor = Color.LightGray,
                            disabledTextColor = Color.Gray
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false }
                    ) {
                        mealTypes.forEach { mealType ->
                            DropdownMenuItem(
                                text = { Text(mealType) },
                                onClick = {
                                    selectedMealType = mealType
                                    showDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Log Meal Button
            Button(
                onClick = {
                    if (uid != null && !isMealLogged) {
                        isLogging = true
                        viewModel.logMeal(uid, meal._id, today, selectedMealType) { success ->
                            isLogging = false
                            if (success) {
                                Toast.makeText(context, "Meal logged successfully!", Toast.LENGTH_SHORT).show()
                                viewModel.getMealPlan(uid)
                            } else {
                                Toast.makeText(context, "Failed to log meal", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                enabled = !isMealLogged && !isLogging,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isMealLogged) Color.Gray else Color(0xFF8FBC8F),
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isLogging) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Logging...", color = Color.White)
                    }
                } else {
                    Text(
                        if (isMealLogged) "Meal Already Logged" else "Log Meal",
                        color = Color.White
                    )
                }
            }
        }
    }
}