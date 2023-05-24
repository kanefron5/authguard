package dev.zabolotskikh.authguard.domain.model

import java.io.Serializable

sealed interface PasscodeResult: Serializable {
    object Succeed : PasscodeResult
    data class Confirmed(val newCode: String) : PasscodeResult
    object LimitReached : PasscodeResult
}