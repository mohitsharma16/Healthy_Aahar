package com.mohit.healthy_aahar.ui.screens.auth

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mohit.healthy_aahar.datastore.UserPreference
import com.mohit.healthy_aahar.model.User
import com.mohit.healthy_aahar.ui.viewmodel.MainViewModel
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import androidx.compose.foundation.BorderStroke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSetupScreen(onSetupComplete: () -> Unit) {
    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current

    val registerResponse by viewModel.registerResponse.observeAsState()
    val error by viewModel.error.observeAsState()

    val uidFlow = remember { UserPreference.getUidFlow(context) }
    val uid by uidFlow.collectAsState(initial = null)

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("Balanced Diet") }
    var activityLevel by remember { mutableStateOf("sedentary") }
    var estimatedCalories by remember { mutableStateOf("") }
    var timePeriod by remember { mutableStateOf("1 Month") }
    var showCalorieModal by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // Handle success
    LaunchedEffect(registerResponse) {
        registerResponse?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
            onSetupComplete()
        }
    }

    // Handle error
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Welcome to Healthy आहार",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Help us get to know you better...",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Personal Information
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            UserInputField(
                label = "First Name",
                value = firstName,
                onValueChange = { firstName = it },
                modifier = Modifier.weight(1f)
            )
            UserInputField(
                label = "Last Name",
                value = lastName,
                onValueChange = { lastName = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            UserInputField(
                label = "Age",
                value = age,
                onValueChange = { age = it },
                modifier = Modifier.weight(1f)
            )
            UserInputField(
                label = "Gender",
                value = gender,
                onValueChange = { gender = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            UserInputField(
                label = "Weight",
                value = weight,
                onValueChange = { weight = it },
                modifier = Modifier.weight(1f)
            )
            UserInputField(
                label = "Height",
                value = height,
                onValueChange = { height = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Activity Level
        Text(
            text = "Select your activity level",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        ActivityLevelGrid(
            selectedLevel = activityLevel,
            onLevelSelected = { activityLevel = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Goals Section
        Text(
            text = "Set Your Goals",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        GoalSelectionRow(
            selectedGoal = goal,
            onGoalSelected = { goal = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Time Period
        Text(
            text = "Choose Time Period",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TimePeriodDropdown(
            selectedPeriod = timePeriod,
            onPeriodSelected = { timePeriod = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Forward Arrow Button (Calculate & Show Modal)
        Button(
            onClick = {
                if (firstName.isNotEmpty() && lastName.isNotEmpty() && age.isNotEmpty() &&
                    gender.isNotEmpty() && weight.isNotEmpty() && height.isNotEmpty()) {
                    estimatedCalories = predictCalories(context, height, weight, age, gender, goal)
                    showCalorieModal = true
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .size(56.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("→", fontSize = 24.sp, color = Color.White)
        }
    }

    // Calorie Modal Overlay
    if (showCalorieModal) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { /* Prevent background clicks */ },
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50).copy(alpha = 0.9f)),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Estimated Calorie Intake",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Text(
                        text = estimatedCalories,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    // Submit Arrow Button in Modal
                    Button(
                        onClick = {
                            showCalorieModal = false
                            if (uid != null) {
                                viewModel.registerUser(
                                    User(
                                        uid = uid!!,
                                        name = "$firstName $lastName",
                                        age = age.toIntOrNull() ?: 0,
                                        gender = gender.trim().lowercase(),
                                        weight = weight.toFloatOrNull() ?: 0f,
                                        height = height.toFloatOrNull() ?: 0f,
                                        activityLevel = activityLevel.trim().lowercase(),
                                        goal = when (goal) {
                                            "Balanced Diet" -> "maintenance"
                                            "Weight Loss" -> "weight_loss"
                                            "Muscle Gain" -> "weight_gain"
                                            else -> "maintenance"
                                        }
                                    )
                                )
                            }
                        },
                        modifier = Modifier.size(56.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text("→", fontSize = 24.sp, color = Color(0xFF4CAF50))
                    }
                }
            }
        }
    }
}

@Composable
fun UserInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF666666),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4CAF50),
                unfocusedBorderColor = Color(0xFFE0E0E0)
            )
        )
    }
}

@Composable
fun ActivityLevelGrid(
    selectedLevel: String,
    onLevelSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val levels = listOf(
            "sedentary" to "Sedentary",
            "light" to "Light",
            "moderate" to "Moderate",
            "active" to "Active"
        )

        levels.forEach { (value, display) ->
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onLevelSelected(value) },
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedLevel == value) Color(0xFF4CAF50) else Color.White
                ),
                border = BorderStroke(
                    1.dp,
                    if (selectedLevel == value) Color(0xFF4CAF50) else Color(0xFFE0E0E0)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = display,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = if (selectedLevel == value) Color.White else Color(0xFF666666)
                )
            }
        }
    }
}

@Composable
fun GoalSelectionRow(
    selectedGoal: String,
    onGoalSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val goals = listOf(
            "Weight Loss" to "🏃‍♀️",
            "Muscle Gain" to "💪",
            "Balanced Diet" to "🥗"
        )

        goals.forEach { (goal, emoji) ->
            GoalCard(
                goal = goal,
                emoji = emoji,
                isSelected = selectedGoal == goal,
                onSelected = { onGoalSelected(goal) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun GoalCard(
    goal: String,
    emoji: String,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onSelected() }
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF4CAF50).copy(alpha = 0.1f) else Color.White
        ),
        border = BorderStroke(
            2.dp,
            if (isSelected) Color(0xFF4CAF50) else Color(0xFFE0E0E0)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = emoji,
                fontSize = 32.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = goal.replace(" ", "\n"),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = if (isSelected) Color(0xFF4CAF50) else Color(0xFF666666)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePeriodDropdown(
    selectedPeriod: String,
    onPeriodSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("1 Month", "3 Months", "6 Months", "1 Year")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedPeriod,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4CAF50),
                unfocusedBorderColor = Color(0xFFE0E0E0)
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onPeriodSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

// Load the tflite model
fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
    val assetFileDescriptor: AssetFileDescriptor = assetManager.openFd(modelPath)
    val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
    val fileChannel = fileInputStream.channel
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, assetFileDescriptor.startOffset, assetFileDescriptor.declaredLength)
}

// Predict calories using tflite
fun predictCalories(context: Context, height: String, weight: String, age: String, gender: String, goal: String): String {
    return try {
        val interpreter = Interpreter(loadModelFile(context.assets, "calorie_intake_model.tflite"))
        val heightFloat = height.toFloatOrNull() ?: return "Invalid Input"
        val weightFloat = weight.toFloatOrNull() ?: return "Invalid Input"
        val ageFloat = age.toFloatOrNull() ?: return "Invalid Input"
        val genderFloat = if (gender.lowercase() == "male") 1f else 0f
        val goalFloat = when (goal) {
            "Weight Loss" -> -1f
            "Balanced Diet" -> 0f
            "Muscle Gain" -> 1f
            else -> return "Invalid Goal"
        }

        val input = arrayOf(floatArrayOf(heightFloat, weightFloat, ageFloat, genderFloat, goalFloat))
        val output = Array(1) { FloatArray(1) }

        interpreter.run(input, output)
        output[0][0].toInt().toString() + " Kcal"
    } catch (e: Exception) {
        "Error: ${e.message}"
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserSetupScreen() {
    UserSetupScreen(onSetupComplete = {})
}