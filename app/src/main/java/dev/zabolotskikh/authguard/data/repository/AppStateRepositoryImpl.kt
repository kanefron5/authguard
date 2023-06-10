package dev.zabolotskikh.authguard.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import dev.zabolotskikh.authguard.preference
import dev.zabolotskikh.authguard.domain.model.AppState
import dev.zabolotskikh.authguard.domain.repository.AppStateRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppStateRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : AppStateRepository {

    private var Preferences.isStarted by preference(booleanPreferencesKey("is_started"))
    private var Preferences.isRemoteMode by preference(booleanPreferencesKey("is_remote_mode"))
    private var Preferences.isPrivateMode by preference(booleanPreferencesKey("is_private_mode"))

    override fun getState() = dataStore.data.map {
        AppState(
            isStarted = it.isStarted ?: false,
            isRemoteMode = it.isRemoteMode ?: false,
            isPrivateMode = it.isPrivateMode ?: false
        )
    }

    override suspend fun update(appState: AppState) {
        dataStore.edit {
            it.isStarted = appState.isStarted
            it.isRemoteMode = appState.isRemoteMode
            it.isPrivateMode = appState.isPrivateMode
        }
    }
}