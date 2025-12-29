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
// daos
package com.realityos.app.data.dao

import androidx.room.*
import com.realityos.app.data.entities.*

@Dao
interface UsageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(event: UsageEventEntity)

    @Query("SELECT * FROM usage_events WHERE dayEpoch = :dayEpoch ORDER BY totalMillis DESC")
    suspend fun getForDay(dayEpoch: Long): List<UsageEventEntity>
}

@Dao
interface RuleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: RuleEntity): Long

    @Update suspend fun update(rule: RuleEntity)

    @Query("SELECT * FROM rules") suspend fun getAll(): List<RuleEntity>
    @Query("SELECT * FROM rules WHERE active = 1") suspend fun getActive(): List<RuleEntity>
}

@Dao
interface CommitEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: CommitEventEntity): Long

    @Query("SELECT * FROM commit_events ORDER BY timestamp DESC LIMIT :limit")
    suspend fun recent(limit: Int): List<CommitEventEntity>
}
