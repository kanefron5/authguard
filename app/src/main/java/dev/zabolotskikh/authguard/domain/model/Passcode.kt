package dev.zabolotskikh.authguard.domain.model

data class Passcode(
    val lastAuthorizedTimestamp: Long = System.currentTimeMillis(),
    val passcodeHash: String
)