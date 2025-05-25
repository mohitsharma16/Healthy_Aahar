package com.mohit.healthy_aahar.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RightSideDrawer(
    isOpen: Boolean,
    onClose: () -> Unit,
    drawerContent: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
        AnimatedVisibility(visible = isOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = onClose)
                    .background(Color.Black.copy(alpha = 0.4f))
            )
        }

        AnimatedVisibility(visible = isOpen) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp)
                    .background(Color.White, shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column {
                    drawerContent()
                }
            }
        }
    }
}
