@file:OptIn(FlowPreview::class)

package dev.zabolotskikh.authenticator.ui.screen.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zabolotskikh.authenticator.domain.model.AppState
import dev.zabolotskikh.authenticator.domain.repository.AppStateRepository
import dev.zabolotskikh.authenticator.domain.repository.ServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val stateRepository: AppStateRepository,
    private val serviceRepository: ServiceRepository
) : ViewModel() {
    fun resetData() = viewModelScope.launch(Dispatchers.IO) {
        stateRepository.update(AppState(isStarted = false, isAuthenticated = false))
        serviceRepository.clear()
    }


    private var clickCounter = 0
    fun easterEgg() {
        if (++clickCounter >= 5) {
            clickCounter = 0
        } else {
            viewModelScope.launch {
                delay(3000)
                if (clickCounter > 0) clickCounter--
            }
        }
    }
}