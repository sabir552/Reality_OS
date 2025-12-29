// app/src/main/java/com/realityos/app/receivers/ReinstallReceiver.kt
package com.realityos.app.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.realityos.app.domain.CommitEventType
import com.realityos.app.domain.CommitLevel
import com.realityos.app.repositories.CommitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReinstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            CoroutineScope(Dispatchers.Default).launch {
                CommitRepository().log(
                    type = CommitEventType.REINSTALL,
                    level = CommitLevel.INITIATE,
                    detail = "App reinstalled"
                )
            }
        }
    }
}
