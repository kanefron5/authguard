package dev.zabolotskikh.passlock.domain.repository

import kotlinx.coroutines.flow.Flow

internal interface PasscodeRepository {
    suspend fun checkPasscode(passcode: String): Boolean

    fun hasPasscode(): Flow<Boolean>

    suspend fun updatePasscode(passcode: String)

    suspend fun deletePasscode()
}