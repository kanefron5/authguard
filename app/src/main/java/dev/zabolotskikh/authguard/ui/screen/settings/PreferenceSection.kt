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
}

fun String.toPreferenceSection(): PreferenceSection {
    if (equals(PreferenceSection.Main())) return PreferenceSection.Main
    if (equals(PreferenceSection.Passcode())) return PreferenceSection.Passcode
    throw IllegalArgumentException("Screen $this not found!")
}