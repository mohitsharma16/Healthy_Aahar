package com.mohit.healthy_aahar.ui.screens.profile

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mohit.healthy_aahar.R
import com.mohit.healthy_aahar.datastore.UserPreference
import com.mohit.healthy_aahar.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onSignOut: () -> Unit = {}) {
    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current
    val uidFlow = remember { UserPreference.getUidFlow(context) }
    val uid by uidFlow.collectAsState(initial = null)
    val userDetails by viewModel.userDetails.observeAsState()

    LaunchedEffect(uid) {
        uid?.let {
            viewModel.fetchUserDetails(it) { name ->
                // You can handle the result of user details here
                // For example, update userDetails with the name
                if (name != null) {
                    // You can handle setting the name to a mutable state variable if needed
                    // Example: userDetails?.name = name
                }
            }
        }
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
                    text = "Profile",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            navigationIcon = {
                IconButton(onClick = { /* Handle back navigation */ }) {
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
            // Profile Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Image
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE57373)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_profile),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(60.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = userDetails?.name ?: "Mohit Sharma",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = userDetails?.goal?.replace("_", " ")?.replaceFirstChar { it.uppercase() } ?: "Weight loss",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "${userDetails?.age ?: "21"}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Cards
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(
                        label = "Height",
                        value = "${userDetails?.height ?: "184"}cm"
                    )
                    StatItem(
                        label = "Weight",
                        value = "${userDetails?.weight ?: "96"}kgs"
                    )
                    StatItem(
                        label = "BMI",
                        value = "24" // Calculate BMI or get from userDetails
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Gender Section
            SectionTitle("Gender")
            DropdownSection(
                selectedValue = userDetails?.gender ?: "Male",
                onValueChange = { /* Handle gender change */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dietary Preferences
            SectionTitle("Dietary Preferences")
            DropdownSection(
                selectedValue = "Keto", // Get from userDetails or preferences
                onValueChange = { /* Handle dietary preference change */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Medical Condition
            SectionTitle("Medical Condition")
            MedicalConditionSection()

            Spacer(modifier = Modifier.height(16.dp))

            // Choose Time Period
            SectionTitle("Choose Time Period")
            DropdownSection(
                selectedValue = "1 Month",
                onValueChange = { /* Handle time period change */ }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Logout Button
            Button(
                onClick = onSignOut,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8BC34A)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Logout",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        color = Color.Black,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSection(
    selectedValue: String,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF8BC34A),
                unfocusedBorderColor = Color.Gray
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Add dropdown items based on the section
            DropdownMenuItem(
                text = { Text(selectedValue) },
                onClick = {
                    onValueChange(selectedValue)
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun MedicalConditionSection() {
    val conditions = listOf("Diabetes", "Blood Pressure", "Thyroid", "High Cholesterol", "Pregnant")
    val selectedConditions = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            .padding(12.dp)
    ) {
        conditions.chunked(2).forEach { rowConditions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowConditions.forEach { condition ->
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedConditions.contains(condition),
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    selectedConditions.add(condition)
                                } else {
                                    selectedConditions.remove(condition)
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF8BC34A)
                            )
                        )
                        Text(
                            text = condition,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }
                }
                // Fill remaining space if odd number of conditions in row
                if (rowConditions.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// **Reusable Profile Field Component** - Keeping for compatibility
@Composable
fun ProfileField(text: String, backgroundColor: Color, width: Dp) {
    Box(
        modifier = Modifier
            .width(width)
            .height(50.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

// **About & Sign Out (Plain Text Style)** - Keeping for compatibility
@Composable
fun ProfileTextOption(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    )
}

// **🔹 Profile Screen Preview**
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onSignOut = {})
}