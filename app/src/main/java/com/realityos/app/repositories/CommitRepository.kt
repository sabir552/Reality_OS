// app/src/main/java/com/realityos/app/repositories/CommitRepository.kt
package com.realityos.app.repositories

import com.realityos.app.data.AppDatabase
import com.realityos.app.data.entities.CommitEventEntity
import com.realityos.app.domain.CommitEventType
import com.realityos.app.domain.CommitLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CommitRepository {

    private val dao = AppDatabase.get().commitEventDao()

    suspend fun log(
        type: CommitEventType,
        level: CommitLevel,
        detail: String
    ) = withContext(Dispatchers.IO) {
        dao.insert(
            CommitEventEntity(
                timestamp = System.currentTimeMillis(),
                type = type,
                level = level,
                detail = detail
            )
        )
    }

    suspend fun recent(limit: Int = 100): List<CommitEventEntity> =
        withContext(Dispatchers.IO) { dao.recent(limit) }
}
