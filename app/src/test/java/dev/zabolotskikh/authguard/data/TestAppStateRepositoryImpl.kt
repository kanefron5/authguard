package dev.zabolotskikh.authguard.data

import dev.zabolotskikh.authguard.domain.model.AppState
import dev.zabolotskikh.authguard.domain.repository.AppStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart

class TestAppStateRepositoryImpl(
    var state: AppState = AppState(
        isStarted = false,
        isRemoteMode = false,
        isPrivateMode = false,
        passcode = null
    )
): AppStateRepository {
    private val flow = MutableSharedFlow<AppState>()

    override fun getState(): Flow<AppState> = flow.onStart { emit(state) }

    override suspend fun update(appState: AppState) {
        state = appState
        flow.emit(state)
    }
}