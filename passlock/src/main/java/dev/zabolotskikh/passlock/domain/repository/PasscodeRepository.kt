package dev.zabolotskikh.passlock.domain.repository

import dev.zabolotskikh.passlock.domain.model.PasscodeCheckStatus
import kotlinx.coroutines.flow.Flow

internal interface PasscodeRepository {
    suspend fun checkPasscode(passcode: String): PasscodeCheckStatus

    fun hasPasscode(): Flow<Boolean>

    fun getRemainingAttempts(): Flow<Int>

    fun getBlockEndTime(): Flow<Long>

    suspend fun updatePasscode(passcode: String)

    suspend fun deletePasscode()
}