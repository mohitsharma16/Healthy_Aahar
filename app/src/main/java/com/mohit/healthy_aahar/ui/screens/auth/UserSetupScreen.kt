package com.mohit.healthy_aahar.ui.screens.auth

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSetupScreen(onSetupComplete: () -> Unit) { // 🔹 Changed from NavController to Lambda
    var step by remember { mutableStateOf(1) }
    //User Inputs
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("Maintain") }
    var timePeriod by remember { mutableStateOf("1 Month") }
    var allergies by remember { mutableStateOf("None") }
    var estimatedCalories by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val context = LocalContext.current
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
                    Text("Select Time Period:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    RadioButtonGroup(selectedValue = timePeriod, options = listOf("1 Month", "3 Months", "6 Months", "1 Year")) { timePeriod = it }
                    Text("Any Allergies?", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    RadioButtonGroup(selectedValue = allergies, options = listOf("Nut", "Dairy", "Gluten", "Soy","None" )) { allergies = it }

                    //display estimated calories
                    Text("Estimated Daily Calorie Intake:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = estimatedCalories.ifEmpty { "Not calculated yet" }, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Blue) }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // **Navigation Buttons**
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
                        onClick = { estimatedCalories = predictCalories(context, height, weight, age, gender, goal) },
                        colors = ButtonDefaults.buttonColors(Color(0xFF8B4513))
                    ) {
                        Text("Calculate")
                    }
                    Button(
                        onClick = { onSetupComplete() },
                        colors = ButtonDefaults.buttonColors(Color(0xFF8B4513))
                    ) {
                        Text("Submit")
                    }
                }
                }
            }
        }
    }


// load the tflite model
fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer{
    val assetFileDescriptor : AssetFileDescriptor = assetManager.openFd(modelPath)
    val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
    val fileChannel = fileInputStream.channel
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, assetFileDescriptor.startOffset, assetFileDescriptor.declaredLength)
}

// function to predict calories using tflite
fun predictCalories(context: Context, height: String, weight :String, age:String, gender: String, goal:String): String{
    return try{
        val interpreter = Interpreter(loadModelFile(context.assets, "calorie_intake_model.tflite"))
        val heightFloat = height.toFloatOrNull() ?: return "Invalid Input"
        val weightFloat = weight.toFloatOrNull() ?: return "Invalid Input"
        val ageFloat = age.toFloatOrNull() ?: return "Invalid Input"
        val genderFloat = if(gender.lowercase() == "male") 1f else 0f
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
    }catch (e: Exception){
        "Error: ${e.message}"
    }

}

// **Reusable Input Field Component**
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

// **Reusable Radio Button Group**
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

// **🔹 User Setup Screen Preview**
@Preview(showBackground = true)
@Composable
fun PreviewUserSetupScreen() {
    UserSetupScreen(onSetupComplete = {})
}
