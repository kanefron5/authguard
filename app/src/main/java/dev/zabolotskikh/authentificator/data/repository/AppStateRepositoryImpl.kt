package dev.zabolotskikh.authentificator.data.repository

import dev.zabolotskikh.authentificator.data.local.dao.AppStateDao
import dev.zabolotskikh.authentificator.data.local.dao.ServiceDao
import dev.zabolotskikh.authentificator.data.local.entities.AppStateEntity
import dev.zabolotskikh.authentificator.data.local.entities.ServiceEntity
import dev.zabolotskikh.authentificator.domain.model.AppState
import dev.zabolotskikh.authentificator.domain.model.Service
import dev.zabolotskikh.authentificator.domain.repository.AppStateRepository
import dev.zabolotskikh.authentificator.domain.repository.ServiceRepository
import kotlinx.coroutines.flow.Flow
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