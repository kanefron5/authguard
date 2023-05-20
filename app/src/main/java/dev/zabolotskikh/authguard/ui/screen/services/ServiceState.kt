package dev.zabolotskikh.authguard.ui.screen.services

import dev.zabolotskikh.authguard.domain.model.GenerationMethod
import dev.zabolotskikh.authguard.domain.model.Service

data class ServiceState(
    val services: List<Service> = emptyList(),
    val isPrivateMode: Boolean = false,
    val isAddingService: Boolean = false,
    val isLoading: Boolean = false,
    val isBadSecret: Boolean = false,
    val privateKey: String = "",
    val name: String = "",
    val method: GenerationMethod = GenerationMethod.TIME
)