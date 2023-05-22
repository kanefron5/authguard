package dev.zabolotskikh.authguard.ui.screen.settings

import dev.zabolotskikh.authguard.R

sealed class PreferenceSection(val title: Int) {
    object Main: PreferenceSection(R.string.settings_title_main)
    object Passcode: PreferenceSection(R.string.settings_title_passcode)
}