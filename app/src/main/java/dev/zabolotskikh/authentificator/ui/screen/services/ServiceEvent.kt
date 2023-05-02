package dev.zabolotskikh.authentificator.ui.screen.services

import dev.zabolotskikh.authentificator.domain.model.GenerationMethod
import dev.zabolotskikh.authentificator.domain.model.Service

interface ServiceEvent {
    object SaveService: ServiceEvent
    data class SetPrivateKey(val privateKey: String): ServiceEvent
    data class SetName(val name: String): ServiceEvent
    data class SetMethod(val method: GenerationMethod): ServiceEvent
    object ShowDialog: ServiceEvent
    object HideDialog: ServiceEvent
    data class DeleteService(val service: Service): ServiceEvent
}