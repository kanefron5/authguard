package dev.zabolotskikh.authguard.ui.screen.settings.sections.main.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.authguard.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AccountState())

    private val _userAccount = authRepository.getUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val state = combine(_state, _userAccount) { state, userAccount ->
        state.copy(userAccount = userAccount)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)


    fun onEvent(event: AccountEvent) {
        when (event) {
            AccountEvent.OnSignOut -> viewModelScope.launch { authRepository.signOut() }
            AccountEvent.OnVerifyEmail -> viewModelScope.launch { authRepository.sendVerificationEmail() }
        }
    }
}