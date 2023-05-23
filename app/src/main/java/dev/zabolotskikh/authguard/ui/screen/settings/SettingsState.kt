package dev.zabolotskikh.authguard.ui.screen.settings

data class SettingsState(
    val passcodeSettingsProcess: Boolean = false,
    val passcodeSettingsCurrent: String = "",
    val passcodeSettingsAttempt: Int = 0,
    val isPasscodeEnabled: Boolean = false,
    val currentSection: PreferenceSection = PreferenceSection.Main
)

fun SettingsState.resetPasscodeFields(): SettingsState = copy(
    passcodeSettingsProcess = false,
    passcodeSettingsCurrent = "",
    passcodeSettingsAttempt = 0,
)