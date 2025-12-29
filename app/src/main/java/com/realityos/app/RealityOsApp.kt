// app/src/main/java/com/realityos/app/RealityOsApp.kt
package com.realityos.app

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.realityos.app.data.AppDatabase
import com.realityos.app.enforcement.RuleEnforcementWorker
import java.util.concurrent.TimeUnit

class RealityOsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(this)
        scheduleEnforcement()
    }

    private fun scheduleEnforcement() {
        val work = PeriodicWorkRequestBuilder<RuleEnforcementWorker>(15, TimeUnit.MINUTES)
            .addTag(RuleEnforcementWorker.WORK_TAG)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            RuleEnforcementWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            work
        )
    }
}
