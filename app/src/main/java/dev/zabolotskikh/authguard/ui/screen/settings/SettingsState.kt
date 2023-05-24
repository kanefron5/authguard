package dev.zabolotskikh.authguard.ui.screen.settings

data class SettingsState(
    val isPasscodeEnabled: Boolean = false,
    val currentSection: PreferenceSection = PreferenceSection.Main
)
