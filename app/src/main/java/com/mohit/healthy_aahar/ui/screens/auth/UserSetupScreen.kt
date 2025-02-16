package com.mohit.healthy_aahar.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSetupScreen(onSetupComplete: () -> Unit) { // 🔹 Changed from NavController to Lambda
    var step by remember { mutableStateOf(1) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var dietPreference by remember { mutableStateOf("Keto") }
    var timePeriod by remember { mutableStateOf("1 Month") }
    var allergies by remember { mutableStateOf("Nut") }

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
                .background(Color.White),
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
                    RadioButtonGroup(selectedValue = dietPreference, options = listOf("Keto", "Vegan", "Vegetarian", "Paleo")) { dietPreference = it }
                }
                3 -> {
                    Text("Select Time Period:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    RadioButtonGroup(selectedValue = timePeriod, options = listOf("1 Month", "3 Months", "6 Months", "1 Year")) { timePeriod = it }
                    Text("Any Allergies?", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    RadioButtonGroup(selectedValue = allergies, options = listOf("Nut", "Dairy", "Gluten", "Soy")) { allergies = it }
                }
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
                    Button(onClick = { onSetupComplete() }, colors = ButtonDefaults.buttonColors(Color(0xFF8B4513))) {
                        Text("Submit")
                    }
                }
            }
        }
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
