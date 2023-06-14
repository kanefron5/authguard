package dev.zabolotskikh.passlock.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.zabolotskikh.passlock.data.ResetBlockWorker
import dev.zabolotskikh.passlock.domain.PasscodeEncoder
import dev.zabolotskikh.passlock.domain.model.PasscodeCheckStatus
import dev.zabolotskikh.passlock.domain.repository.CurrentTimeRepository
import dev.zabolotskikh.passlock.domain.repository.PasscodeRepository
import dev.zabolotskikh.passlock.preference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.max


private const val BLOCK_TIME = 5 * 60 * 1000L
private const val MAX_ATTEMPT_COUNT = 5L

private val resetBlockKeys = arrayOf("passcode_blocked_until", "passcode_attempt_count")

internal class PasscodeRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val encoder: PasscodeEncoder,
    private val currentTimeRepository: CurrentTimeRepository,
    private val workManager: WorkManager,
) : PasscodeRepository {
    private var Preferences.passcodeHash by preference(stringPreferencesKey("passcode_hash"))
    private var Preferences.passcodeTime by preference(longPreferencesKey("passcode_last_entered"))
    private var Preferences.attemptCount by preference(longPreferencesKey("passcode_attempt_count"))
    private var Preferences.blockedUntil by preference(longPreferencesKey("passcode_blocked_until"))


    override suspend fun checkPasscode(
        passcode: String
    ): PasscodeCheckStatus {
        val preferences = dataStore.data.first()

        val currentHash = preferences.passcodeHash
        var currentAttemptCount = (preferences.attemptCount ?: 0) + 1
        val currentBlockedUntil = preferences.blockedUntil ?: 0

        if (currentTimeRepository.now() < currentBlockedUntil) {
            return PasscodeCheckStatus.BlockedUntil(currentBlockedUntil)
        } else if (currentBlockedUntil != 0L) {
            if (isBlockExpired()) {
                resetBlock()
                currentAttemptCount = 1
            }
        }

        // Записываем количество попыток
        dataStore.edit { prefs ->
            prefs.attemptCount = currentAttemptCount
        }

        return if (currentHash == calculateHash(passcode)) {
            resetBlock()
            PasscodeCheckStatus.Success
        } else {
            if (currentAttemptCount == MAX_ATTEMPT_COUNT) {
                PasscodeCheckStatus.BlockedUntil(setBlock())
            } else {
                PasscodeCheckStatus.NotMatch
            }
        }
    }

    override fun hasPasscode(): Flow<Boolean> {
        return dataStore.data.map { it.passcodeHash != null }
    }

    override fun getRemainingAttempts() = dataStore.data.map {
        if (isBlockExpired()) resetBlock()
        max(MAX_ATTEMPT_COUNT - (it.attemptCount ?: 0), 0).toInt()
    }

    override fun getBlockEndTime() = dataStore.data.map {
        if (isBlockExpired()) resetBlock()
        it.blockedUntil ?: 0
    }

    override suspend fun updatePasscode(passcode: String) {
        dataStore.edit { prefs ->
            prefs.passcodeHash = calculateHash(passcode)
            prefs.passcodeTime = currentTimeRepository.now()
        }
    }

    override suspend fun deletePasscode() {
        dataStore.edit {
            it.passcodeHash = null
            it.passcodeTime = null
            it.blockedUntil = null
            it.attemptCount = null
        }
    }

    private fun calculateHash(passcode: String): String {
        return encoder.encode(passcode)?.decodeToString() ?: passcode
    }

    private suspend fun isBlockExpired(): Boolean {
        val preferences = dataStore.data.first()
        return currentTimeRepository.now() >= (preferences.blockedUntil
            ?: 0) && MAX_ATTEMPT_COUNT == preferences.attemptCount
    }

    private suspend fun resetBlock() {
        dataStore.edit { prefs ->
            prefs.blockedUntil = null
            prefs.attemptCount = null
        }
    }


    private suspend fun setBlock(): Long {
        val endBlockTime = currentTimeRepository.now() + BLOCK_TIME
        dataStore.edit { prefs ->
            prefs.blockedUntil = endBlockTime
        }

        try {
            workManager.enqueue(
                OneTimeWorkRequestBuilder<ResetBlockWorker>().setInputData(
                    workDataOf(
                        ResetBlockWorker.PARAM_RESET_BLOCK_KEYS to resetBlockKeys
                    )
                ).setInitialDelay(BLOCK_TIME, TimeUnit.MILLISECONDS).build()
            )
        } catch (ignored: Exception) {
        }

        return endBlockTime
    }

}