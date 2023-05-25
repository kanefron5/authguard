package dev.zabolotskikh.passlock.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.passlock.di.LibraryScope
import dev.zabolotskikh.passlock.domain.repository.PasscodeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PasscodeViewModel @Inject constructor(
    private val passcodeRepository: PasscodeRepository,
    @LibraryScope private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    val state = MutableStateFlow(PasscodeState())

    fun onEvent(event: PasscodeEvent) {
        when (event) {
            PasscodeEvent.SavePasscode -> viewModelScope.launch(ioDispatcher) {
                passcodeRepository.updatePasscode(state.value.passcode)
                state.update { it.copy(isConfirmed = true) }
            }

            is PasscodeEvent.SetPasscode -> {
                if (event.passcode.isNotBlank()) {
                    if (state.value.passcode.isNotBlank() && state.value.passcode != event.passcode) {
                        // show error
                        println("error")
                    } else {
                        state.update {
                            it.copy(
                                passcode = event.passcode,
                                attemptCount = state.value.attemptCount + 1
                            )
                        }
                    }
                }
            }

            PasscodeEvent.Cancel -> state.update { it.copy(isCancelled = true) }
            is PasscodeEvent.EnterPasscode -> state.update { it.copy(isSucceed = true) }

        }
    }
}