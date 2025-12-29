// app/src/main/java/com/realityos/app/domain/Models.kt
package com.realityos.app.domain

data class UsageSummary(
    val packageName: String,
    val totalMinutes: Int
)

data class Rule(
    val id: Long,
    val targetPackage: String,
    val dailyLimitMinutes: Int,
    val punishmentType: PunishmentType,
    val active: Boolean,
    val level: CommitLevel
)
