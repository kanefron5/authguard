package dev.zabolotskikh.passlock.domain.model

internal sealed interface PasscodeCheckStatus {
    object Success: PasscodeCheckStatus
    object NotMatch: PasscodeCheckStatus
    object NoPasscode: PasscodeCheckStatus
    data class BlockedUntil(val time: Long): PasscodeCheckStatus
}