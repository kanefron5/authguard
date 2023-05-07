package dev.zabolotskikh.authguard.ui.screen.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.authguard.domain.model.AppState
import dev.zabolotskikh.authguard.domain.repository.AppStateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val repository: AppStateRepository
) : ViewModel() {
    val state = repository.getState().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), null
    )

    fun startLocal() = viewModelScope.launch(Dispatchers.IO) {
        repository.update(
            state.value?.copy(isStarted = true) ?: AppState(
                isStarted = true, isAuthenticated = false
            )
        )
    }
}