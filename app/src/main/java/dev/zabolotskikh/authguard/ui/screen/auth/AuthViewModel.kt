package dev.zabolotskikh.authguard.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.authguard.domain.model.AppState
import dev.zabolotskikh.authguard.domain.repository.AppStateRepository
import dev.zabolotskikh.authguard.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val stateRepository: AppStateRepository,
    private val authRepository: AuthRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    private val _appState = stateRepository.getState().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), null
    )

    val state = _state
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    fun onEvent(event: AuthEvent) {
        when (event) {
            AuthEvent.StartLocal -> startLocal()
        }
    }

    private fun startLocal() = viewModelScope.launch(coroutineDispatcher) {
        stateRepository.update((_appState.value ?: AppState()).copy(isStarted = true))
    }
}