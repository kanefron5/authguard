package dev.zabolotskikh.authguard.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.authguard.domain.model.AppState
import dev.zabolotskikh.authguard.domain.repository.AppStateRepository
import dev.zabolotskikh.authguard.domain.repository.PasscodeRepository
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
    private val passcodeRepository: PasscodeRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    private val _appState = stateRepository.getState().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), null
    )

    val state = combine(_appState, _state) { appState, settingsState ->
        settingsState.copy(isPasscodeEnabled = appState?.passcode != null)
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsState()
    )

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.BuildNumberClick -> {}
            SettingsEvent.ResetData -> resetData()
            is SettingsEvent.ChangeSection -> _state.update { it.copy(currentSection = event.section) }
            SettingsEvent.DeletePasscode -> viewModelScope.launch(ioDispatcher) {
                passcodeRepository.deletePasscode()
            }
        }
    }


    private fun resetData() = viewModelScope.launch(ioDispatcher) {
        stateRepository.update(AppState())
        serviceRepository.clear()
    }
}