package dev.zabolotskikh.authguard.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.authguard.domain.model.AppState
import dev.zabolotskikh.authguard.domain.repository.AppStateRepository
import dev.zabolotskikh.authguard.domain.repository.ChangelogRepository
import dev.zabolotskikh.authguard.domain.repository.ServiceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val stateRepository: AppStateRepository,
    private val serviceRepository: ServiceRepository,
    changelogRepository: ChangelogRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    val state = MutableStateFlow(SettingsState(changelog = changelogRepository.get()))

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.BuildNumberClick -> {}
            SettingsEvent.ResetData -> resetData()
            is SettingsEvent.ChangeSection -> state.update { it.copy(currentSection = event.section) }
        }
    }


    private fun resetData() = viewModelScope.launch(ioDispatcher) {
        stateRepository.update(AppState())
        serviceRepository.clear()
    }
}