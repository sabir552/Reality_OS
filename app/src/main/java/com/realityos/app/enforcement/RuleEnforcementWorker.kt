// app/src/main/java/com/realityos/app/enforcement/RuleEnforcementWorker.kt
package com.realityos.app.enforcement

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.realityos.app.domain.CommitEventType
import com.realityos.app.domain.CommitLevel
import com.realityos.app.domain.PunishmentType
import com.realityos.app.repositories.CommitRepository
import com.realityos.app.repositories.RuleRepository
import com.realityos.app.repositories.UsageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RuleEnforcementWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val usageRepo = UsageRepository(context)
    private val ruleRepo = RuleRepository()
    private val commitRepo = CommitRepository()
    private val punishments = PunishmentManager(context)

    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {
        usageRepo.refreshTodayUsage()
        val usage = usageRepo.getTodayUsage()
        val rules = ruleRepo.getActive()

        for (rule in rules) {
            val u = usage.firstOrNull { it.packageName == rule.targetPackage } ?: continue
            if (u.totalMinutes > rule.dailyLimitMinutes) {
                when (rule.punishmentType) {
                    PunishmentType.GREYSCALE -> punishments.applyGreyscale()
                    PunishmentType.APP_BLOCK -> punishments.blockApp(rule.targetPackage)
                }
                commitRepo.log(
                    type = CommitEventType.RULE_VIOLATION,
                    level = rule.level,
                    detail = "${rule.targetPackage}:${u.totalMinutes}/${rule.dailyLimitMinutes}"
                )
            }
        }
        Result.success()
    }

    companion object {
        const val WORK_NAME = "rule_enforcement"
        const val WORK_TAG = "rule_enforcement_tag"
    }
}
