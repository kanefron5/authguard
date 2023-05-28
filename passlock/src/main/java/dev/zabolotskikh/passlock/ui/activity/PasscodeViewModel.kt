package dev.zabolotskikh.passlock.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.passlock.di.LibraryScope
import dev.zabolotskikh.passlock.domain.model.PasscodeCheckStatus
import dev.zabolotskikh.passlock.domain.repository.PasscodeRepository
import dev.zabolotskikh.passlock.ui.activity.PasscodeResult.BLOCKED
import dev.zabolotskikh.passlock.ui.activity.PasscodeResult.CANCELLED
import dev.zabolotskikh.passlock.ui.activity.PasscodeResult.CONFIRMED
import dev.zabolotskikh.passlock.ui.activity.PasscodeResult.REJECTED
import dev.zabolotskikh.passlock.ui.activity.PasscodeResult.SUCCEED
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
internal class PasscodeViewModel @Inject constructor(
    private val passcodeRepository: PasscodeRepository,
    @LibraryScope private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _state = MutableStateFlow(PasscodeState())
    private val _remainingAttemptsCount = passcodeRepository.getRemainingAttempts().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), 0
    )
    private val _blockEndTime = passcodeRepository.getBlockEndTime().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), 0
    )

    val state = combine(
        _state, _remainingAttemptsCount, _blockEndTime
    ) { state, remainingAttemptsCount, blockEndTime ->
        state.copy(
            remainingAttemptsCount = remainingAttemptsCount,
            isBlockedUntil = blockEndTime,
            passcodeCheckStatus = if (blockEndTime > System.currentTimeMillis()) BLOCKED else state.passcodeCheckStatus
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PasscodeState())

    fun onEvent(event: PasscodeEvent) {
        when (event) {
            PasscodeEvent.SavePasscode -> viewModelScope.launch(ioDispatcher) {
                passcodeRepository.updatePasscode(_state.value.passcode)
                _state.update { it.copy(passcodeCheckStatus = CONFIRMED) }
            }

            is PasscodeEvent.SetPasscode -> {
                if (event.passcode.isNotBlank()) {
                    if (_state.value.passcode.isNotBlank() && _state.value.passcode != event.passcode) {
                        // show error
                        println("error")
                    } else {
                        _state.update {
                            it.copy(
                                passcode = event.passcode,
                                attemptCount = _state.value.attemptCount + 1
                            )
                        }
                    }
                }
            }

            PasscodeEvent.Cancel -> _state.update { it.copy(passcodeCheckStatus = CANCELLED) }
            is PasscodeEvent.EnterPasscode -> viewModelScope.launch(ioDispatcher) {
                when (passcodeRepository.checkPasscode(event.passcode)) {
                    is PasscodeCheckStatus.BlockedUntil -> _state.update {
                        it.copy(passcodeCheckStatus = BLOCKED)
                    }

                    PasscodeCheckStatus.NoPasscode -> _state.update {
                        it.copy(passcodeCheckStatus = CANCELLED)
                    }

                    PasscodeCheckStatus.NotMatch -> _state.update {
                        it.copy(passcodeCheckStatus = REJECTED)
                    }

                    PasscodeCheckStatus.Success -> _state.update {
                        it.copy(passcodeCheckStatus = SUCCEED)
                    }
                }
            }
        }
    }
}