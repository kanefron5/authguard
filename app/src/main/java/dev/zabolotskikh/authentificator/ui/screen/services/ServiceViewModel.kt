package dev.zabolotskikh.authentificator.ui.screen.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.authentificator.OtpInstance
import dev.zabolotskikh.authentificator.domain.model.GenerationMethod
import dev.zabolotskikh.authentificator.domain.model.Service
import dev.zabolotskikh.authentificator.domain.repository.ServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.timer

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


    private var generationJob: Job? = null

    fun startGeneration() {
        generationJob = viewModelScope.launch(Dispatchers.IO) {
            timer(period = 1000) {
                _state.update { it.copy(services = OtpInstance(_services.value).calculate()) }
            }
        }
    }

    fun stopGeneration() = generationJob?.cancel()

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

                if (privateKey.isBlank() || name.isBlank()) return

                viewModelScope.launch {
                    repository.insert(Service(name, privateKey, generationMethod = method))
                }

                _state.update {
                    it.copy(
                        isAddingService = false,
                        privateKey = "",
                        name = "",
                        method = GenerationMethod.TIME
                    )
                }
            }

            ServiceEvent.ShowDialog -> _state.update { it.copy(isAddingService = true) }
            ServiceEvent.HideDialog -> _state.update { it.copy(isAddingService = false) }
        }
    }
}
