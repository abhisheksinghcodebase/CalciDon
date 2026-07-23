package com.example.ui.theme

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

enum class AppThemeMode(val label: String) {
    DARK_CYBER("Cyber Dark"),
    MINIMAL_LIGHT("Clean Light"),
    MATRIX_GREEN("Matrix Neon")
}

private val DarkCyberColorScheme = darkColorScheme(
    primary = CyberPrimary,
    onPrimary = Color.Black,
    secondary = CyberSecondary,
    onSecondary = Color.Black,
    tertiary = CyberTertiary,
    background = DarkCyberBackground,
    onBackground = Color.White,
    surface = DarkCyberSurface,
    onSurface = Color.White,
    surfaceVariant = DarkCyberSurfaceVariant,
    onSurfaceVariant = Color(0xFF94A3B8)
)

private val MinimalLightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    secondary = LightSecondary,
    onSecondary = Color.White,
    tertiary = LightTertiary,
    background = LightBackground,
    onBackground = Color(0xFF0F172A),
    surface = LightSurface,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF475569)
)

private val MatrixGreenColorScheme = darkColorScheme(
    primary = MatrixPrimary,
    onPrimary = Color.Black,
    secondary = MatrixSecondary,
    onSecondary = Color.Black,
    background = MatrixBackground,
    onBackground = Color(0xFFECFDF5),
    surface = MatrixSurface,
    onSurface = Color(0xFFECFDF5),
    surfaceVariant = Color(0xFF1C2541),
    onSurfaceVariant = Color(0xFF6EE7B7)
)

@Composable
fun CalciDonTheme(
    themeMode: AppThemeMode = AppThemeMode.DARK_CYBER,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        AppThemeMode.DARK_CYBER -> DarkCyberColorScheme
        AppThemeMode.MINIMAL_LIGHT -> MinimalLightColorScheme
        AppThemeMode.MATRIX_GREEN -> MatrixGreenColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
