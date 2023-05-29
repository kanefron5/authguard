package dev.zabolotskikh.passlock.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.zabolotskikh.passlock.BuildConfig
import dev.zabolotskikh.passlock.data.ResetBlockWorker
import dev.zabolotskikh.passlock.domain.PasscodeEncoder
import dev.zabolotskikh.passlock.domain.model.PasscodeCheckStatus
import dev.zabolotskikh.passlock.domain.repository.CurrentTimeRepository
import dev.zabolotskikh.passlock.domain.repository.PasscodeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.max


private const val BLOCK_TIME = 5 * 60 * 1000L
private const val MAX_ATTEMPT_COUNT = 5L

private val PASSCODE_HASH_NAME = stringPreferencesKey("passcode_hash")
private val PASSCODE_TIME_NAME = longPreferencesKey("passcode_last_entered")
private val PASSCODE_ATTEMPT_COUNT_NAME = longPreferencesKey("passcode_attempt_count")
private val PASSCODE_BLOCKED_UNTIL_NAME = longPreferencesKey("passcode_blocked_until")

private val resetBlockKeys =
    arrayOf(PASSCODE_BLOCKED_UNTIL_NAME.name, PASSCODE_ATTEMPT_COUNT_NAME.name)

private const val LOG_TAG = "PasscodeRepositoryImpl"

internal class PasscodeRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val encoder: PasscodeEncoder,
    private val currentTimeRepository: CurrentTimeRepository,
    private val workManager: WorkManager,
) : PasscodeRepository {
    private val Preferences.passcodeHash: String?
        get() = this[PASSCODE_HASH_NAME]

    private val Preferences.attemptCount: Long
        get() = this[PASSCODE_ATTEMPT_COUNT_NAME] ?: 0

    private val Preferences.blockedUntil: Long
        get() = this[PASSCODE_BLOCKED_UNTIL_NAME] ?: 0

    override suspend fun checkPasscode(
        passcode: String
    ): PasscodeCheckStatus {
        val preferences = dataStore.data.first()

        val currentHash = preferences.passcodeHash
        var currentAttemptCount = preferences.attemptCount + 1
        val currentBlockedUntil = preferences.blockedUntil

        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, "Preferences: $preferences")
            if (currentHash == calculateHash(passcode)) {
                resetBlock()
                return PasscodeCheckStatus.Success
            }
        }

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
            prefs[PASSCODE_ATTEMPT_COUNT_NAME] = currentAttemptCount
        }

        return if (currentHash == calculateHash(passcode)) {
            resetBlock()
            PasscodeCheckStatus.Success
        } else {
            if (currentAttemptCount == MAX_ATTEMPT_COUNT) {
                PasscodeCheckStatus.BlockedUntil(setBlock())
            }
            PasscodeCheckStatus.NotMatch
        }
    }

    override fun hasPasscode(): Flow<Boolean> {
        return dataStore.data.map { it.passcodeHash != null }
    }

    override fun getRemainingAttempts() = dataStore.data.map {
        if (isBlockExpired()) resetBlock()
        max(MAX_ATTEMPT_COUNT - it.attemptCount, 0).toInt()
    }

    override fun getBlockEndTime() = dataStore.data.map {
        if (isBlockExpired()) resetBlock()
        it.blockedUntil
    }

    override suspend fun updatePasscode(passcode: String) {
        dataStore.edit { prefs ->
            prefs[PASSCODE_HASH_NAME] = calculateHash(passcode)
            prefs[PASSCODE_TIME_NAME] = currentTimeRepository.now()
        }
    }

    override suspend fun deletePasscode() {
        dataStore.edit {
            it.remove(PASSCODE_HASH_NAME)
            it.remove(PASSCODE_TIME_NAME)
            it.remove(PASSCODE_BLOCKED_UNTIL_NAME)
            it.remove(PASSCODE_ATTEMPT_COUNT_NAME)
        }
    }

    private fun calculateHash(passcode: String): String {
        return encoder.encode(passcode)?.decodeToString() ?: passcode
    }

    private suspend fun isBlockExpired(): Boolean {
        val preferences = dataStore.data.first()
        return currentTimeRepository.now() >= preferences.blockedUntil && MAX_ATTEMPT_COUNT == preferences.attemptCount
    }

    private suspend fun resetBlock() {
        dataStore.edit { prefs ->
            resetBlockKeys.forEach { prefs.remove(stringPreferencesKey(it)) }
        }
    }


    private suspend fun setBlock(): Long {
        val endBlockTime = currentTimeRepository.now() + BLOCK_TIME
        dataStore.edit { prefs ->
            prefs[PASSCODE_BLOCKED_UNTIL_NAME] = endBlockTime
        }

        try {
            workManager.enqueue(
                OneTimeWorkRequestBuilder<ResetBlockWorker>().setInputData(workDataOf(ResetBlockWorker.PARAM_RESET_BLOCK_KEYS to resetBlockKeys))
                    .setInitialDelay(BLOCK_TIME, TimeUnit.MILLISECONDS).build()
            )
        } catch (ignored: Exception) {
        }


        return endBlockTime
    }

}