package dev.zabolotskikh.authenticator.ui.screen.services

import dev.zabolotskikh.authenticator.domain.model.GenerationMethod
import dev.zabolotskikh.authenticator.domain.model.Service

data class ServiceState(
    val services: List<Service> = emptyList(),
    val isAddingService: Boolean = false,
    val privateKey: String = "",
    val name: String = "",
    val method: GenerationMethod = GenerationMethod.TIME
)