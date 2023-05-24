package dev.zabolotskikh.authguard.domain.model

import java.io.Serializable

data class PasscodeOptions(
    val maxAttemptCount: Int = 1,
    val isPasscodeSetup: Boolean = false,
): Serializable
