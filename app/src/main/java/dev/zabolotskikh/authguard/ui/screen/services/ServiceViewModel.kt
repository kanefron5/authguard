package dev.zabolotskikh.authguard.ui.screen.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.authguard.OtpInstance
import dev.zabolotskikh.authguard.domain.model.GenerationMethod
import dev.zabolotskikh.authguard.domain.model.Service
import dev.zabolotskikh.authguard.domain.repository.AppStateRepository
import dev.zabolotskikh.authguard.domain.repository.OtpRepository
import dev.zabolotskikh.authguard.domain.repository.ServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val repository: ServiceRepository,
    private val stateRepository: AppStateRepository,
    otpRepository: OtpRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ServiceState())
    private val _services = otpRepository.getAllServices().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), null
    )
    private val _appState = stateRepository.getState().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), null
    )

    val state = combine(_state, _services, _appState) { state, services, appState ->
        state.copy(
            isLoading = services == null,
            services = services ?: emptyList(),
            isPrivateMode = appState?.isPrivateMode ?: false
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), ServiceState()
    )

    fun onEvent(event: ServiceEvent) {
        when (event) {
            is ServiceEvent.DeleteService -> viewModelScope.launch {
                repository.delete(event.service)
            }

            is ServiceEvent.SetName -> _state.update { it.copy(name = event.name) }
            is ServiceEvent.SetPrivateKey -> {
                _state.update {
                    it.copy(
                        privateKey = event.privateKey,
                        isBadSecret = !OtpInstance.checkSecret(event.privateKey)
                    )
                }
            }

            is ServiceEvent.SetMethod -> _state.update { it.copy(method = event.method) }
            ServiceEvent.SaveService -> {
                val privateKey = state.value.privateKey
                val method = state.value.method
                val name = state.value.name

                if (privateKey.isBlank() || name.isBlank()) return

                viewModelScope.launch {
                    repository.insert(Service(name, privateKey, generationMethod = method))
                }

                _state.update {
                    it.copy(
                        isAddingService = false,
                        isBadSecret = false,
                        privateKey = "",
                        name = "",
                        method = GenerationMethod.TIME
                    )
                }
            }

            ServiceEvent.ShowDialog -> _state.update { it.copy(isAddingService = true) }
            ServiceEvent.HideDialog -> _state.update {
                it.copy(
                    isAddingService = false,
                    isBadSecret = false,
                    privateKey = "",
                    name = "",
                    method = GenerationMethod.TIME
                )
            }

            ServiceEvent.PrivateModeOff -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _appState.value?.apply {
                        stateRepository.update(copy(isPrivateMode = false))
                    }
                }
            }

            ServiceEvent.PrivateModeOn -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _appState.value?.apply {
                        stateRepository.update(copy(isPrivateMode = true))
                    }
                }
            }

            is ServiceEvent.AddToFavorite -> viewModelScope.launch(Dispatchers.IO) {
                repository.update(event.service.copy(isFavorite = true))
            }

            is ServiceEvent.RemoveFromFavorite -> viewModelScope.launch(Dispatchers.IO) {
                repository.update(event.service.copy(isFavorite = false))
            }
        }
    }
}
