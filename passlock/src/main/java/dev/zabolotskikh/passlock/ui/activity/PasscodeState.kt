package dev.zabolotskikh.passlock.ui.activity

internal data class PasscodeState(
    val passcodeCheckStatus: PasscodeResult? = null,
    val passcode: String = "",
    val attemptCount: Int = 0,
    val remainingAttemptsCount: Int = 0,
    val isBlockedUntil: Long = 0,
)
