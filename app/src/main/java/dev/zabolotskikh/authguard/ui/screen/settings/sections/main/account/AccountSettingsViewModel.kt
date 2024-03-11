package dev.zabolotskikh.authguard.ui.screen.settings.sections.main.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.auth.di.FirebaseAuthScope
import dev.zabolotskikh.authguard.domain.repository.AuthRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    @FirebaseAuthScope private val authRepository: AuthRepository,
) : ViewModel() {
    fun onEvent(event: AccountEvent) {
        when (event) {
            AccountEvent.OnSignOut -> viewModelScope.launch { authRepository.signOut() }
            AccountEvent.OnVerifyEmail -> viewModelScope.launch { authRepository.sendVerificationEmail() }
        }
    }
}