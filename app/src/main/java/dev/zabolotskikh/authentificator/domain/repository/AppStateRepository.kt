package dev.zabolotskikh.authentificator.domain.repository

import dev.zabolotskikh.authentificator.domain.model.AppState
import kotlinx.coroutines.flow.Flow

interface AppStateRepository {

    fun getState(): Flow<AppState>

    suspend fun update(appState: AppState)
}