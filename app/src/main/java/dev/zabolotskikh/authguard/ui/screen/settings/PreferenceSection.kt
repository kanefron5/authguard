package dev.zabolotskikh.authguard.ui.screen.settings

import androidx.annotation.StringRes
import dev.zabolotskikh.authguard.R

sealed class PreferenceSection(
    @StringRes val title: Int,
    private val route: String,
    val back: PreferenceSection?
) {
    operator fun invoke() = route

    object Main : PreferenceSection(R.string.settings_title_main, "main", null)
    object Passcode : PreferenceSection(R.string.settings_title_passcode, "passcode", Main)
    object PasscodeSetup : PreferenceSection(R.string.settings_title_passcode, "passcode_setup", Passcode)
}

fun String.toPreferenceSection(): PreferenceSection {
    if (equals(PreferenceSection.Main())) return PreferenceSection.Main
    if (equals(PreferenceSection.Passcode())) return PreferenceSection.Passcode
    if (equals(PreferenceSection.PasscodeSetup())) return PreferenceSection.PasscodeSetup
    throw IllegalArgumentException("Screen $this not found!")
}