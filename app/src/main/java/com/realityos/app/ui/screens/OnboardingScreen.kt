// app/src/main/java/com/realityos/app/ui/screens/OnboardingScreen.kt
package com.realityos.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.realityos.app.ui.RootAction
import com.realityos.app.ui.RootState

@Composable
fun OnboardingScreen(
    state: RootState,
    onAction: (RootAction) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Reality OS", style = MaterialTheme.typography.headlineMedium)
            Text(
                "You define rules. Reality OS enforces them without resets.",
                style = MaterialTheme.typography.bodyMedium
            )

            Text("Required permissions", style = MaterialTheme.typography.titleMedium)

            PermissionRow(
                title = "Usage access",
                granted = state.hasUsagePermission,
                onClick = { onAction(RootAction.RequestUsagePermission) }
            )
            PermissionRow(
                title = "Accessibility",
                granted = state.hasAccessibilityPermission,
                onClick = { onAction(RootAction.RequestAccessibilityPermission) }
            )
            PermissionRow(
                title = "Overlay (for blocking)",
                granted = state.hasOverlayPermission,
                onClick = { onAction(RootAction.RequestOverlayPermission) }
            )
        }

        Button(
            enabled = state.hasUsagePermission && state.hasAccessibilityPermission,
            onClick = onContinue,
            modifier = Modifier.align(Alignment.End)
        ) { Text("Continue") }
    }
}

@Composable
private fun PermissionRow(
    title: String,
    granted: Boolean,
    onClick: () -> Unit
) {
    Column(Modifier.padding(vertical = 8.dp)) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        Text(
            if (granted) "Granted" else "Required",
            style = MaterialTheme.typography.bodySmall
        )
        if (!granted) {
            Spacer(Modifier.height(4.dp))
            Button(onClick = onClick) { Text("Grant") }
        }
    }
}
