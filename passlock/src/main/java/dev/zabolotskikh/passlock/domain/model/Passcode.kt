package dev.zabolotskikh.passlock.domain.model

internal data class Passcode(
    val lastAuthorizedTimestamp: Long = System.currentTimeMillis(),
    val passcodeHash: String
)