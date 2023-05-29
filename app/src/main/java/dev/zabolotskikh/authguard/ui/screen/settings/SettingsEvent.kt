package dev.zabolotskikh.authguard.ui.screen.settings

sealed interface SettingsEvent {

    data class ChangeSection(val section: PreferenceSection): SettingsEvent
    object ResetData : SettingsEvent
    object BuildNumberClick: SettingsEvent
}