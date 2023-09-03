package dev.zabolotskikh.auth.ui.activity.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.auth.di.EmailValidator
import dev.zabolotskikh.auth.di.PasswordValidator
import dev.zabolotskikh.authguard.domain.model.AppState
import dev.zabolotskikh.authguard.domain.repository.AppStateRepository
import dev.zabolotskikh.authguard.domain.repository.AuthRepository
import dev.zabolotskikh.authguard.domain.repository.DataValidator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val stateRepository: AppStateRepository,
    private val authRepository: AuthRepository,
    private val coroutineDispatcher: CoroutineDispatcher,
    @PasswordValidator private val passwordValidator: DataValidator<String>,
    @EmailValidator private val emailValidator: DataValidator<String>
) : ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    private val _appState = stateRepository.getState().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), null
    )

    val state = _state.map {
        it.copy(
            isPasswordValid = passwordValidator.check(it.password),
            isEmailValid = emailValidator.check(it.email)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    init {
        viewModelScope.launch {
            authRepository.isAuthenticated().filter { it }.collectLatest {
                stateRepository.update(
                    (_appState.value ?: AppState()).copy(
                        isStarted = true,
                        isRemoteMode = true
                    )
                )
            }
        }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            AuthEvent.OnForgotPassword -> viewModelScope.launch(coroutineDispatcher) {
                safeExecuteWithProgress {
                    authRepository.sendResetPasswordEmail(_state.value.email)
                }
                _state.update { it.copy(isResetPasswordDialogShown = false) }
            }

            AuthEvent.OnSignIn -> viewModelScope.launch(coroutineDispatcher) {
                safeExecuteWithProgress {
                    authRepository.signIn(_state.value.email, _state.value.password)
                }
            }

            AuthEvent.OnSignUp -> viewModelScope.launch(coroutineDispatcher) {
                safeExecuteWithProgress {
                    authRepository.signUp(_state.value.email, _state.value.password)
                }
            }

            is AuthEvent.OnEditEmail -> _state.update { it.copy(email = event.value) }
            is AuthEvent.OnEditPassword -> _state.update { it.copy(password = event.value) }
            is AuthEvent.OnForgotPasswordDialog -> _state.update {
                it.copy(isResetPasswordDialogShown = event.isShown)
            }

            AuthEvent.OnDismissError -> _state.update { it.copy(error = null) }
        }
    }

    private suspend fun safeExecuteWithProgress(action: suspend () -> Unit) {
        try {
            _state.updateProgress(true)
            action.invoke()
        } catch (e: Exception) {
            e.printStackTrace()
            _state.update { it.copy(error = e) }
        } finally {
            _state.updateProgress(false)
        }
    }

    private fun MutableStateFlow<AuthState>.updateProgress(value: Boolean) {
        update { it.copy(isProgress = value) }
    }
}