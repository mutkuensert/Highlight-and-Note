package com.mutkuensert.highlightandnote.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = Color.Black,
    surface = Color.Black
)

private val LightColorScheme = lightColorScheme(
    background = Color.White,
    surface = Color.White
)

class AppColorScheme(
    val shadow: Color
)

@Suppress("UnusedReceiverParameter")
val MaterialTheme.appColors: AppColorScheme
    @Composable
    get() = AppColorScheme(shadow = getThemeColor(Color.Black, Color.White))

@Composable
private fun getThemeColor(
    light: Color,
    dark: Color = light,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
): Color {
    return if (isDarkTheme) dark else light
}

@Composable
fun HighlightAndNoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}