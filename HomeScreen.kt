// HomeScreen.kt
package com.realityos.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.realityos.app.ui.RootState
import com.realityos.app.ui.Routes

@Composable
fun HomeScreen(
    state: RootState,
    nav: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Reality OS", style = MaterialTheme.typography.headlineMedium)
        Text("Level: ${state.level}", style = MaterialTheme.typography.bodyMedium)
        Text("Streak: ${state.streak} days", style = MaterialTheme.typography.bodyMedium)
        Text("XP: ${state.xp}", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            HomeCard("Rules", "Define and review your app limits.") {
                nav.navigate(Routes.Rules)
            }
            HomeCard("Modes", "View your commitment level.") {
                nav.navigate(Routes.Modes)
            }
        }

        HomeCard("History", "Permanent log. No reset.") {
            nav.navigate(Routes.History)
        }
    }
}

@Composable
private fun HomeCard(
    title: String,
    body: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(body, style = MaterialTheme.typography.bodySmall)
        }
    }
}
    
