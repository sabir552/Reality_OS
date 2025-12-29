// HistoryScreen.kt
package com.realityos.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.realityos.app.data.entities.CommitEventEntity
import com.realityos.app.repositories.CommitRepository
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(nav: NavHostController) {
    val repo = remember { CommitRepository() }
    var events by remember { mutableStateOf<List<CommitEventEntity>>(emptyList()) }

    LaunchedEffect(Unit) {
        events = repo.recent(200)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("History", style = MaterialTheme.typography.headlineSmall)
        Text("Permanent record. There is no reset.",
            style = MaterialTheme.typography.bodySmall)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(events) { e -> HistoryItem(e) }
        }
    }
}

@Composable
private fun HistoryItem(e: CommitEventEntity) {
    val date = remember {
        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date(e.timestamp))
    }
    Column {
        Text(
            "$date • ${e.type} • ${e.level}",
            style = MaterialTheme.typography.bodyMedium
        )
        if (e.detail.isNotBlank()) {
            Text(e.detail, style = MaterialTheme.typography.bodySmall)
        }
    }
}
