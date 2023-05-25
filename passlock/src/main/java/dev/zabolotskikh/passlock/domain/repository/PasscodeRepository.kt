package dev.zabolotskikh.passlock.domain.repository

import dev.zabolotskikh.passlock.domain.model.PasscodeCheckStatus
import kotlinx.coroutines.flow.Flow

internal interface PasscodeRepository {
    suspend fun checkPasscode(passcode: String, maxAttemptCount: Int): PasscodeCheckStatus

    fun hasPasscode(): Flow<Boolean>

    suspend fun updatePasscode(passcode: String)

    suspend fun deletePasscode()
}