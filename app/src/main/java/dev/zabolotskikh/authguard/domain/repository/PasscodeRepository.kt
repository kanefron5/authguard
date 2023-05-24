package dev.zabolotskikh.authguard.domain.repository

interface PasscodeRepository {
    suspend fun checkPasscode(hash: String): Boolean

    suspend fun updatePasscode(passcode: String)
}