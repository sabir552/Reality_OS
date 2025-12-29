// app/src/main/java/com/realityos/app/ui/theme/Theme.kt
package com.realityos.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val Colors = darkColorScheme(
    primary = RealityPrimary,
    onPrimary = RealityOnPrimary,
    background = RealityBackground,
    onBackground = RealityOnBackground,
    surface = RealitySurface,
    onSurface = RealityOnBackground,
    secondary = RealityMuted
)

@Composable
fun RealityOsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Colors,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
