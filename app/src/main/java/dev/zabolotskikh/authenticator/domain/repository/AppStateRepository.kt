package dev.zabolotskikh.authenticator.domain.repository

import dev.zabolotskikh.authenticator.domain.model.AppState
import kotlinx.coroutines.flow.Flow

interface AppStateRepository {

    fun getState(): Flow<AppState>

    suspend fun update(appState: AppState)
}