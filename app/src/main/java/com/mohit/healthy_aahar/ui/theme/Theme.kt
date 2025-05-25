package com.mohit.healthy_aahar.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Primary500,
    onPrimary = Color.White,
    secondary = Primary200,
    onSecondary = Color.Black,
    tertiary = Success500,
    background = Neutral900,
    surface = Neutral800,
    onBackground = Text100,
    onSurface = Text50,
    error = Error500,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Primary500,
    onPrimary = Color.White,
    secondary = Primary100,
    onSecondary = Color.Black,
    tertiary = Success400,
    background = Neutral50,
    surface = Neutral100,
    onBackground = Text900,
    onSurface = Text800,
    error = Error500,
    onError = Color.White
)

@Composable
fun HealthyAaharTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
