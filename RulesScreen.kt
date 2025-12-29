// RulesScreen.kt
package com.realityos.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.realityos.app.domain.CommitLevel
import com.realityos.app.domain.PunishmentType
import com.realityos.app.domain.Rule
import com.realityos.app.repositories.RuleRepository
import kotlinx.coroutines.launch

@Composable
fun RulesScreen(nav: NavHostController) {
    val repo = remember { RuleRepository() }
    var rules by remember { mutableStateOf<List<Rule>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        rules = repo.getAll()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Rules", style = MaterialTheme.typography.headlineSmall)

        rules.forEach { r ->
            Card {
                Column(Modifier.padding(12.dp)) {
                    Text(r.targetPackage, style = MaterialTheme.typography.bodyLarge)
                    Text("Limit: ${r.dailyLimitMinutes} min/day",
                        style = MaterialTheme.typography.bodySmall)
                    Text("Punishment: ${r.punishmentType}",
                        style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        var pkg by remember { mutableStateOf("") }
        var mins by remember { mutableStateOf("30") }
        var punishment by remember { mutableStateOf(PunishmentType.APP_BLOCK) }

        Text("Add rule", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = pkg,
            onValueChange = { pkg = it },
            label = { Text("Package name (e.g. com.instagram.android)") }
        )
        OutlinedTextField(
            value = mins,
            onValueChange = { mins = it.filter(Char::isDigit) },
            label = { Text("Daily limit (minutes)") }
        )

        Text("Punishment", style = MaterialTheme.typography.bodyMedium)
        Column {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                RadioButton(
                    selected = punishment == PunishmentType.APP_BLOCK,
                    onClick = { punishment = PunishmentType.APP_BLOCK }
                )
                Text("App block overlay")
            }
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                RadioButton(
                    selected = punishment == PunishmentType.GREYSCALE,
                    onClick = { punishment = PunishmentType.GREYSCALE }
                )
                Text("Greyscale nudge")
            }
        }

        Button(onClick = {
            val m = mins.toIntOrNull() ?: 0
            if (pkg.isNotBlank() && m > 0) {
                scope.launch {
                    repo.createRule(pkg, m, punishment, CommitLevel.INITIATE)
                    rules = repo.getAll()
                }
            }
        }) { Text("Save rule") }
    }
}
