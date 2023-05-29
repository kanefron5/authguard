package dev.zabolotskikh.authguard.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.authguard.domain.model.AppState
import dev.zabolotskikh.authguard.domain.repository.AppStateRepository
import dev.zabolotskikh.authguard.domain.repository.ServiceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val stateRepository: AppStateRepository,
    private val serviceRepository: ServiceRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    val state = MutableStateFlow(SettingsState())

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