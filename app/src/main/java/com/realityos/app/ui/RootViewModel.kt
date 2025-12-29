// app/src/main/java/com/realityos/app/ui/RootViewModel.kt
package com.realityos.app.ui

import android.app.Application
import android.app.AppOpsManager
import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.realityos.app.domain.CommitLevel
import com.realityos.app.prefs.UserPrefs
import com.realityos.app.repositories.RuleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class RootState(
    val hasUsagePermission: Boolean = false,
    val hasAccessibilityPermission: Boolean = false,
    val hasOverlayPermission: Boolean = false,
    val level: CommitLevel = CommitLevel.INITIATE,
    val streak: Int = 0,
    val xp: Int = 0
)

sealed class RootAction {
    object RequestUsagePermission : RootAction()
    object RequestAccessibilityPermission : RootAction()
    object RequestOverlayPermission : RootAction()
    object PurchaseElite : RootAction()
}

class RootViewModel(app: Application) : AndroidViewModel(app) {

    private val ctx: Context get() = getApplication()
    private val prefs = UserPrefs(ctx)
    private val rules = RuleRepository()

    private val _state = MutableStateFlow(RootState())
    val state: StateFlow<RootState> = _state

    init {
        viewModelScope.launch {
            combine(
                prefs.level,
                prefs.streak,
                prefs.xp
            ) { level, streak, xp ->
                RootState(
                    hasUsagePermission = hasUsageAccess(),
                    hasAccessibilityPermission = hasAccessibility(),
                    hasOverlayPermission = hasOverlay(),
                    level = level,
                    streak = streak,
                    xp = xp
                )
            }.collect { _state.value = it }
        }

        viewModelScope.launch {
            if (rules.getAll().isEmpty()) {
                // starter example rule
                rules.createRule(
                    pkg = "com.instagram.android",
                    minutes = 30,
                    punishment = com.realityos.app.domain.PunishmentType.APP_BLOCK,
                    level = CommitLevel.INITIATE
                )
            }
        }
    }

    fun onElitePurchased() {
        viewModelScope.launch { prefs.setLevel(CommitLevel.ELITE) }
    }

    private fun hasUsageAccess(): Boolean {
        val appOps = ctx.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                ctx.packageName
            )
        } else {
            appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), ctx.packageName)
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun hasAccessibility(): Boolean {
        val enabled = Settings.Secure.getString(
            ctx.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return enabled.contains(ctx.packageName)
    }

    private fun hasOverlay(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(ctx)
        } else true
}
