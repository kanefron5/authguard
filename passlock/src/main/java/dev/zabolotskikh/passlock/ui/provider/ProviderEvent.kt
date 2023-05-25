package dev.zabolotskikh.passlock.ui.provider

import androidx.lifecycle.Lifecycle

internal sealed interface ProviderEvent {
    data class ChangeLifecycle(val event: Lifecycle.Event) : ProviderEvent
    object OnValidate : ProviderEvent
}