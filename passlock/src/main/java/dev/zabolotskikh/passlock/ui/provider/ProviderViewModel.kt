package dev.zabolotskikh.passlock.ui.provider

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.passlock.domain.repository.PasscodeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val LOG_TAG = "PassLockProvider"

@HiltViewModel
internal class ProviderViewModel @Inject constructor(
    passcodeRepository: PasscodeRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ProviderState())
    private val _hasPasscode = passcodeRepository.hasPasscode().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), false
    )
    val state = combine(_state, _hasPasscode) { _state, hasPasscode ->
        _state.copy(hasPasscode = hasPasscode)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


    fun onEvent(event: ProviderEvent) {
        when (event) {
            is ProviderEvent.ChangeLifecycle -> {
                Log.d(LOG_TAG, "Event: ${event.event}; Has passcode: ${state.value?.hasPasscode}")
                if (event.event == Lifecycle.Event.ON_CREATE) {
                    _state.update { it.copy(isLocked = true) }
                }
            }

            ProviderEvent.OnValidate -> {
                _state.update { it.copy(isLocked = false) }
            }
        }
    }
}