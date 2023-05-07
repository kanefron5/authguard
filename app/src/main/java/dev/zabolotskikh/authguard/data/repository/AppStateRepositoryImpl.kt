package dev.zabolotskikh.authguard.data.repository

import dev.zabolotskikh.authguard.data.local.dao.AppStateDao
import dev.zabolotskikh.authguard.data.local.entities.AppStateEntity
import dev.zabolotskikh.authguard.domain.model.AppState
import dev.zabolotskikh.authguard.domain.repository.AppStateRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppStateRepositoryImpl @Inject constructor(
    private val appStateDao: AppStateDao
) : AppStateRepository {
    override fun getState() = appStateDao.getState().map {
        AppState(it?.isStarted ?: false, it?.isAuthenticated ?: false)
    }

    override suspend fun update(appState: AppState) =
        appStateDao.updateState(AppStateEntity(appState.isStarted, appState.isAuthenticated))
}