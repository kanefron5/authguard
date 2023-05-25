package dev.zabolotskikh.passlock.ui.activity

sealed interface PasscodeEvent {
    data class EnterPasscode(val passcode: String, val maxAttemptCount: Int): PasscodeEvent
    data class SetPasscode(val passcode: String): PasscodeEvent
    object SavePasscode: PasscodeEvent
    object Cancel: PasscodeEvent
}