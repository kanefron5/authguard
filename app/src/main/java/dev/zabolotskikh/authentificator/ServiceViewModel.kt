package dev.zabolotskikh.authentificator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.authentificator.data.local.entities.ServiceEntity
import dev.zabolotskikh.authentificator.domain.model.Service
import dev.zabolotskikh.authentificator.domain.repository.ServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val repository: ServiceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ServiceState())
    private val _services = repository.getAllServices().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    val state = combine(_state, _services) { state, services ->
        state.copy(services = OtpInstance(services).calculate())
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ServiceState()
    )

    fun startGeneration() = viewModelScope.launch(Dispatchers.IO) {
        while (true) {
            _state.update { _state.value.copy(services = state.value.services) }
            delay(1000)
        }
    }

    fun onEvent(event: ServiceEvent) {
        when (event) {
            is ServiceEvent.DeleteService -> viewModelScope.launch {
                repository.delete(event.service)
            }
            is ServiceEvent.SetName -> _state.update { it.copy(name = event.name) }
            is ServiceEvent.SetPrivateKey -> _state.update { it.copy(privateKey = event.privateKey) }
            is ServiceEvent.SetMethod -> _state.update { it.copy(method = event.method) }
            ServiceEvent.SaveService -> {
                val privateKey = state.value.privateKey
                val method = state.value.method
                val name = state.value.name

                if (privateKey.isBlank() || method.isBlank() || name.isBlank()) return

                viewModelScope.launch {
                    repository.insert(Service(name, privateKey))
                }

                _state.update {
                    it.copy(
                        isAddingService = false,
                        privateKey = "",
                        name = "",
                        method = ""
                    )
                }
            }

            ServiceEvent.ShowDialog -> _state.update { it.copy(isAddingService = true) }
            ServiceEvent.HideDialog -> _state.update { it.copy(isAddingService = false) }
        }
    }
}