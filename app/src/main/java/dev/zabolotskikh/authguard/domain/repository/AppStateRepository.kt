package dev.zabolotskikh.authguard.domain.repository

import dev.zabolotskikh.authguard.domain.model.AppState
import kotlinx.coroutines.flow.Flow

interface AppStateRepository {

    fun getState(): Flow<AppState>

    suspend fun update(appState: AppState)
}