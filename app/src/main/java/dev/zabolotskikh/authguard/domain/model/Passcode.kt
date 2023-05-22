package dev.zabolotskikh.authguard.domain.model

data class Passcode(
    val lastAuthorizedTimestamp: Long,
    val passcodeHash: String
)