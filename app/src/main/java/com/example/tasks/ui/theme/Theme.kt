package com.example.tasks.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = primaryColor,
    primaryVariant = primaryDarkColor,
    secondary = secondaryColor,
    background = primaryLightColor,
)

@Composable
fun TasksTheme(content: @Composable () -> Unit) {
    val colors = LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}