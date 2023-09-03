package dev.zabolotskikh.auth.ui.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.authguard.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class ProviderViewModel @Inject constructor(
    authRepository: AuthRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ProviderState())
    private val _userAccount = authRepository.getUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val state = combine(_state, _userAccount) { state, userAccount ->
        state.copy(userAccount = userAccount)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

}