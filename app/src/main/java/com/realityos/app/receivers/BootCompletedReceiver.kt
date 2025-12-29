// app/src/main/java/com/realityos/app/receivers/BootCompletedReceiver.kt
package com.realityos.app.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.realityos.app.RealityOsApp

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Application.onCreate will re-schedule WorkManager
            (context.applicationContext as? RealityOsApp)?.let { /* no-op */ }
        }
    }
}
