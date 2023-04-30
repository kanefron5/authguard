package dev.zabolotskikh.authentificator

import dev.zabolotskikh.authentificator.domain.model.Service

data class ServiceState(
    val services: List<Service> = emptyList(),
    val isAddingService: Boolean = false,
    val privateKey: String = "",
    val name: String = "",
    val method: String = ""
)