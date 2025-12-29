// app/src/main/java/com/realityos/app/prefs/UserPrefs.kt
package com.realityos.app.prefs

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.realityos.app.domain.CommitLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

object Keys {
    val LEVEL = intPreferencesKey("level")
    val STREAK = intPreferencesKey("streak")
    val XP = intPreferencesKey("xp")
}

class UserPrefs(private val context: Context) {

    val level: Flow<CommitLevel> = context.dataStore.data.map { prefs ->
        when (prefs[Keys.LEVEL] ?: 0) {
            1 -> CommitLevel.DISCIPLINED
            2 -> CommitLevel.ELITE
            else -> CommitLevel.INITIATE
        }
    }

    val streak: Flow<Int> = context.dataStore.data.map { it[Keys.STREAK] ?: 0 }
    val xp: Flow<Int> = context.dataStore.data.map { it[Keys.XP] ?: 0 }

    suspend fun setLevel(level: CommitLevel) {
        context.dataStore.edit { prefs ->
            prefs[Keys.LEVEL] = when (level) {
                CommitLevel.INITIATE -> 0
                CommitLevel.DISCIPLINED -> 1
                CommitLevel.ELITE -> 2
            }
        }
    }

    suspend fun addDayXp(earnedXp: Int) {
        context.dataStore.edit { prefs ->
            val xp = prefs[Keys.XP] ?: 0
            prefs[Keys.XP] = xp + earnedXp
        }
    }
}
