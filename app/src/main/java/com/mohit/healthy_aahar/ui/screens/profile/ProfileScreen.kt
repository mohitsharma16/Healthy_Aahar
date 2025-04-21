package com.mohit.healthy_aahar.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(Color(0xFFF8F8F8)) // Subtle Background Color
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // **Profile Image & Name Row**
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // **Profile Picture**
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEFEFEF)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // **Name Card**
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFD9E4DD)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text =userDetails?.name ?: "User Name",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // **BMI & Stats Row**
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // **BMI Section**
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFD9E4DD)), // Soft Green
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "BMI Graph Here",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // **Four Cards in a Grid**
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row {
                    ProfileField(text = "${userDetails?.height ?: "Height"} cm", backgroundColor = Color(0xFFE3D5E4), width = 80.dp)
                    Spacer(modifier = Modifier.width(12.dp))
                    ProfileField(text = "Age ${userDetails?.age ?: "Age"}", backgroundColor = Color(0xFFE6D4C7), width = 80.dp)
                }
                Row {
                    ProfileField(text = userDetails?.gender ?: "Gender", backgroundColor = Color(0xFFD9E4DD), width = 80.dp)
                    Spacer(modifier = Modifier.width(12.dp))
                    ProfileField(text = "${userDetails?.weight ?: "Weight"} pd", backgroundColor = Color(0xFFE3D5E4), width = 80.dp)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // **Tabbed Information Sections (Properly Spaced & Left Aligned)**
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Even spacing
        ) {
            ProfileField(
                text = "Goal -  ${userDetails?.goal?.replace("_", " ")?.capitalize() ?: "Goals"}",
                backgroundColor = Color(0xFFD9CBE3),
                width = 320.dp
            )
            ProfileField(text = "Activity Level - ${ userDetails?.activity_level?:"Activity Level"}", backgroundColor = Color(0xFFECE3D5), width = 320.dp)
            ProfileField(text = "BMR - ${(userDetails?.bmr ?:"BMR")}", backgroundColor = Color(0xFFD9E4DD), width = 320.dp)
            ProfileField(text = "TDEE - ${(userDetails?.tdee ?:"TDEE")}", backgroundColor = Color(0xFFE6D4C7), width = 320.dp)
            ProfileField(text = "Medical Conditions", backgroundColor = Color(0xFFD9E4DD), width = 320.dp)
        }

        Spacer(modifier = Modifier.weight(1f)) // Pushes About & Sign Out to Bottom

        // **About & Sign Out Options**
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            ProfileTextOption(text = "About", onClick = { /* Navigate to About Page */ })
            ProfileTextOption(text = "Sign Out", onClick = onSignOut)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// **Reusable Profile Field Component**
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

// **About & Sign Out (Plain Text Style)**
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
    ProfileScreen( onSignOut = {})
}