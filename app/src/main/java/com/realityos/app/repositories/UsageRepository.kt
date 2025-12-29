// app/src/main/java/com/realityos/app/repositories/UsageRepository.kt
package com.realityos.app.repositories

import android.app.usage.UsageStatsManager
import android.content.Context
import com.realityos.app.data.AppDatabase
import com.realityos.app.data.entities.UsageEventEntity
import com.realityos.app.domain.UsageSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class UsageRepository(private val context: Context) {

    private val db = AppDatabase.get()
    private val usageDao = db.usageDao()
    private val usageStats =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    suspend fun refreshTodayUsage() = withContext(Dispatchers.IO) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val start = cal.timeInMillis
        val end = System.currentTimeMillis()
        val dayEpoch = start / 1000

        val stats = usageStats.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            start,
            end
        ) ?: return@withContext

        for (s in stats) {
            if (s.totalTimeInForeground <= 0) continue
            usageDao.upsert(
                UsageEventEntity(
                    packageName = s.packageName,
                    dayEpoch = dayEpoch,
                    totalMillis = s.totalTimeInForeground
                )
            )
        }
    }

    suspend fun getTodayUsage(): List<UsageSummary> = withContext(Dispatchers.IO) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val dayEpoch = cal.timeInMillis / 1000
        usageDao.getForDay(dayEpoch).map {
            UsageSummary(
                packageName = it.packageName,
                totalMinutes = (it.totalMillis / 60000).toInt()
            )
        }
    }
}
