package dev.zabolotskikh.authentificator

import dev.zabolotskikh.authentificator.domain.model.Service

interface ServiceEvent {
    object SaveService: ServiceEvent
    data class SetPrivateKey(val privateKey: String): ServiceEvent
    data class SetName(val name: String): ServiceEvent
    data class SetMethod(val method: String): ServiceEvent
    object ShowDialog: ServiceEvent
    object HideDialog: ServiceEvent
    data class DeleteService(val service: Service): ServiceEvent
}