package dev.zabolotskikh.authentificator.ui.screen.services

import dev.zabolotskikh.authentificator.domain.model.GenerationMethod
import dev.zabolotskikh.authentificator.domain.model.Service

data class ServiceState(
    val services: List<Service> = emptyList(),
    val isAddingService: Boolean = false,
    val privateKey: String = "",
    val name: String = "",
    val method: GenerationMethod = GenerationMethod.TIME
)