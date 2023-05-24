package dev.zabolotskikh.authguard.ui.screen.passcode

sealed interface PasscodeEvent {
    data class SetPasscode(val passcode: String): PasscodeEvent
    object SavePasscode: PasscodeEvent
    object Cancel: PasscodeEvent
}