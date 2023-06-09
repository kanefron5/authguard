package dev.zabolotskikh.passlock.ui.provider

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.passlock.domain.repository.PasscodeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOG_TAG = "PassLockProvider"

@HiltViewModel
internal class ProviderViewModel @Inject constructor(
    private val passcodeRepository: PasscodeRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _state = MutableStateFlow(ProviderState())
    private val _hasPasscode = passcodeRepository.hasPasscode().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), false
    )
    val state = combine(_state, _hasPasscode) { state, hasPasscode ->
        state.copy(hasPasscode = hasPasscode)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


    fun onEvent(event: ProviderEvent) {
        when (event) {
            is ProviderEvent.ChangeLifecycle -> {
                if (event.event == Lifecycle.Event.ON_CREATE) {
                    viewModelScope.launch(ioDispatcher) {
                        val isLocked = passcodeRepository.hasPasscode().first()
                        if (isLocked) _state.update { it.copy(isLocked = true) }
                    }
                }
            }

            ProviderEvent.OnValidate -> {
                _state.update { it.copy(isLocked = false) }
            }
        }
    }
}