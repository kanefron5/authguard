package dev.zabolotskikh.authguard.ui.screen.settings

import dev.zabolotskikh.authguard.domain.model.Release

data class SettingsState(
    val currentSection: PreferenceSection = PreferenceSection.Main,
    val changelog: List<Release> = emptyList(),
)
