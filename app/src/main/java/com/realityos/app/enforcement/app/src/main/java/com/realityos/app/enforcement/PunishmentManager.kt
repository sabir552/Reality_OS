// app/src/main/java/com/realityos/app/enforcement/PunishmentManager.kt
package com.realityos.app.enforcement

import android.content.Context
import android.content.Intent
import android.provider.Settings

class PunishmentManager(private val context: Context) {

    fun applyGreyscale() {
        // Play-safe MVP: open display settings as a neutral nudge.
        val intent = Intent(Settings.ACTION_DISPLAY_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun blockApp(packageName: String) {
        val intent = Intent(context, EnforcementOverlayService::class.java).apply {
            putExtra(EnforcementOverlayService.EXTRA_PACKAGE, packageName)
        }
        context.startService(intent)
    }
}
