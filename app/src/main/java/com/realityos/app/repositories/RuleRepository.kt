// app/src/main/java/com/realityos/app/repositories/RuleRepository.kt
package com.realityos.app.repositories

import com.realityos.app.data.AppDatabase
import com.realityos.app.data.entities.RuleEntity
import com.realityos.app.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RuleRepository {

    private val dao = AppDatabase.get().ruleDao()

    suspend fun createRule(
        pkg: String,
        minutes: Int,
        punishment: PunishmentType,
        level: CommitLevel
    ) = withContext(Dispatchers.IO) {
        dao.insert(
            RuleEntity(
                targetPackage = pkg,
                dailyLimitMinutes = minutes,
                punishmentType = punishment,
                active = true,
                level = level
            )
        )
    }

    suspend fun getAll(): List<Rule> = withContext(Dispatchers.IO) {
        dao.getAll().map {
            Rule(
                id = it.id,
                targetPackage = it.targetPackage,
                dailyLimitMinutes = it.dailyLimitMinutes,
                punishmentType = it.punishmentType,
                active = it.active,
                level = it.level
            )
        }
    }

    suspend fun getActive(): List<Rule> = withContext(Dispatchers.IO) {
        dao.getActive().map {
            Rule(
                id = it.id,
                targetPackage = it.targetPackage,
                dailyLimitMinutes = it.dailyLimitMinutes,
                punishmentType = it.punishmentType,
                active = it.active,
                level = it.level
            )
        }
    }
}
