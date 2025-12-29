// ModesScreen.kt
package com.realityos.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.realityos.app.domain.CommitLevel
import com.realityos.app.ui.RootState

@Composable
fun ModesScreen(
    state: RootState,
    nav: NavHostController,
    onPurchaseElite: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Modes", style = MaterialTheme.typography.headlineSmall)

        ModeCard(
            "Initiate",
            "Default level. Rules, app blocking, and permanent history.",
            state.level == CommitLevel.INITIATE
        )
        ModeCard(
            "Disciplined",
            "Hardcore rules and Beast Mode (to be enabled).",
            state.level == CommitLevel.DISCIPLINED
        )
        ModeCard(
            "Elite Commit",
            "Paid, irreversible commitment. Unlockable via one-time purchase.",
            state.level == CommitLevel.ELITE
        )

        if (state.level != CommitLevel.ELITE) {
            Button(onClick = onPurchaseElite) {
                Text("Unlock Elite (one-time)")
            }
        }
    }
}

@Composable
private fun ModeCard(title: String, body: String, active: Boolean) {
    Card {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = title + if (active) " (current)" else "",
                style = MaterialTheme.typography.titleMedium
            )
            Text(body, style = MaterialTheme.typography.bodySmall)
        }
    }
}
