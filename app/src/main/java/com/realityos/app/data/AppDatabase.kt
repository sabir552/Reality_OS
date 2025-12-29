// app/src/main/java/com/realityos/app/data/AppDatabase.kt
package com.realityos.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.realityos.app.data.dao.CommitEventDao
import com.realityos.app.data.dao.RuleDao
import com.realityos.app.data.dao.UsageDao
import com.realityos.app.data.entities.CommitEventEntity
import com.realityos.app.data.entities.RuleEntity
import com.realityos.app.data.entities.UsageEventEntity

@Database(
    entities = [UsageEventEntity::class, RuleEntity::class, CommitEventEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usageDao(): UsageDao
    abstract fun ruleDao(): RuleDao
    abstract fun commitEventDao(): CommitEventDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun init(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "reality_os.db"
                        ).build()
                    }
                }
            }
        }

        fun get(): AppDatabase =
            requireNotNull(INSTANCE) { "AppDatabase not initialized" }
    }
}
// entities
package com.realityos.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.realityos.app.domain.CommitEventType
import com.realityos.app.domain.CommitLevel
import com.realityos.app.domain.PunishmentType

@Entity(tableName = "usage_events")
data class UsageEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val packageName: String,
    val dayEpoch: Long,
    val totalMillis: Long
)

@Entity(tableName = "rules")
data class RuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val targetPackage: String,
    val dailyLimitMinutes: Int,
    val punishmentType: PunishmentType,
    val active: Boolean,
    val level: CommitLevel
)

@Entity(tableName = "commit_events")
data class CommitEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val type: CommitEventType,
    val level: CommitLevel,
    val detail: String
)
