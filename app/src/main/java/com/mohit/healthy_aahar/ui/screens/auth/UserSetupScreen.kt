package com.mohit.healthy_aahar.ui.screens.auth

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSetupScreen(onSetupComplete: () -> Unit) {
    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current

    val registerResponse by viewModel.registerResponse.observeAsState()
    val error by viewModel.error.observeAsState()

    val uidFlow = remember { UserPreference.getUidFlow(context) }
    val uid by uidFlow.collectAsState(initial = null)

    var step by remember { mutableStateOf(1) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("Maintain") }
    var activityLevel by remember { mutableStateOf("sedentary") }
    var allergies by remember { mutableStateOf("None") }
    var estimatedCalories by remember { mutableStateOf("") }

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

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("User Setup") })
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp)
                .background(Color.White)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Healthy Aahar", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B4513))
            Text("Step $step of 3", fontSize = 16.sp, modifier = Modifier.padding(8.dp))

            Spacer(modifier = Modifier.height(16.dp))

            when (step) {
                1 -> {
                    UserInputField("First Name", firstName) { firstName = it }
                    UserInputField("Last Name", lastName) { lastName = it }
                    UserInputField("Age", age) { age = it }
                    UserInputField("Gender", gender) { gender = it }
                }
                2 -> {
                    UserInputField("Weight", weight) { weight = it }
                    UserInputField("Height", height) { height = it }
                    Text("Diet Preference:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    RadioButtonGroup(selectedValue = goal, options = listOf("Weight Loss", "Maintain", "Weight Gain")) { goal = it }
                }
                3 -> {
                    Text("Select your Activity Level:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    RadioButtonGroup(selectedValue = activityLevel, options = listOf("sedentary", "light", "moderate", "active")) { activityLevel = it }
//                    Text("Any Allergies?", fontSize = 18.sp, fontWeight = FontWeight.Bold)
//                    RadioButtonGroup(selectedValue = allergies, options = listOf("Nut", "Dairy", "Gluten", "Soy", "None")) { allergies = it }

                    Text("Estimated Daily Calorie Intake:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = estimatedCalories.ifEmpty { "Not calculated yet" }, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Blue)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (step > 1) {
                    Button(onClick = { step-- }, colors = ButtonDefaults.buttonColors(Color.Gray)) {
                        Text("Previous")
                    }
                }
                if (step < 3) {
                    Button(onClick = { step++ }, colors = ButtonDefaults.buttonColors(Color(0xFF8B4513))) {
                        Text("Next")
                    }
                } else {
                    Button(
                        onClick = {
                            estimatedCalories = predictCalories(context, height, weight, age, gender, goal)
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFF8B4513))
                    ) {
                        Text("Calculate")
                    }
                    Button(
                        onClick = {
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
                                            "Maintain" -> "maintenance"
                                            "Weight Loss" -> "weight_loss"
                                            "Weight Gain" -> "weight_gain"
                                            else -> "maintenance"
                                        }
                                    )
                                )
                            }

                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFF8B4513))
                    ) {
                        Text("Submit")
                    }
                }
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
            "Maintain" -> 0f
            "Weight Gain" -> 1f
            else -> return "Invalid Goal"
        }

        val input = arrayOf(floatArrayOf(heightFloat, weightFloat, ageFloat, genderFloat, goalFloat))
        val output = Array(1) { FloatArray(1) }

        interpreter.run(input, output)
        output[0][0].toInt().toString() + "Kcal"
    } catch (e: Exception) {
        "Error: ${e.message}"
    }
}

@Composable
fun UserInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@Composable
fun RadioButtonGroup(selectedValue: String, options: List<String>, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                RadioButton(
                    selected = (option == selectedValue),
                    onClick = { onValueChange(option) }
                )
                Text(option, fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserSetupScreen() {
    UserSetupScreen(onSetupComplete = {})
}
