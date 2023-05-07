package dev.zabolotskikh.authenticator.data.repository

import dev.zabolotskikh.authenticator.data.local.dao.AppStateDao
import dev.zabolotskikh.authenticator.data.local.entities.AppStateEntity
import dev.zabolotskikh.authenticator.domain.model.AppState
import dev.zabolotskikh.authenticator.domain.repository.AppStateRepository
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