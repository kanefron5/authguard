package dev.zabolotskikh.authguard.ui.screen.services

import dev.zabolotskikh.authguard.domain.model.GenerationMethod
import dev.zabolotskikh.authguard.domain.model.Service

sealed interface ServiceEvent {
    object SaveService : ServiceEvent
    data class SetPrivateKey(val privateKey: String) : ServiceEvent
    data class SetName(val name: String) : ServiceEvent
    data class SetMethod(val method: GenerationMethod) : ServiceEvent
    object ShowDialog : ServiceEvent
    object HideDialog : ServiceEvent
    data class DeleteService(val service: Service) : ServiceEvent
    data class AddToFavorite(val service: Service) : ServiceEvent
    data class RemoveFromFavorite(val service: Service) : ServiceEvent
    object PrivateModeOn : ServiceEvent
    object PrivateModeOff : ServiceEvent
}