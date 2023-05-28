package dev.zabolotskikh.passlock.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.zabolotskikh.passlock.BuildConfig
import dev.zabolotskikh.passlock.domain.PasscodeEncoder
import dev.zabolotskikh.passlock.domain.model.Passcode
import dev.zabolotskikh.passlock.domain.model.PasscodeCheckStatus
import dev.zabolotskikh.passlock.domain.repository.PasscodeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.math.max


private const val STORE_NAME = "passcode"
private const val BLOCK_TIME = 1 * 60 * 1000
private const val MAX_ATTEMPT_COUNT = 5L

private val PASSCODE_HASH_NAME = stringPreferencesKey("passcode_hash")
private val PASSCODE_TIME_NAME = longPreferencesKey("passcode_last_entered")
private val PASSCODE_ATTEMPT_COUNT_NAME = longPreferencesKey("passcode_attempt_count")
private val PASSCODE_BLOCKED_UNTIL_NAME = longPreferencesKey("passcode_blocked_until")

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = STORE_NAME)

private const val LOG_TAG = "PasscodeRepositoryImpl"

internal class PasscodeRepositoryImpl @Inject constructor(
    private val context: Context, private val encoder: PasscodeEncoder
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
        val preferences = context.dataStore.data.first()

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

        if (System.currentTimeMillis() < currentBlockedUntil) {
            return PasscodeCheckStatus.BlockedUntil(currentBlockedUntil)
        } else if (currentBlockedUntil != 0L) {
            if (isBlockExpired()) {
                resetBlock()
                currentAttemptCount = 1
            }
        }

        // Записываем количество попыток
        context.dataStore.edit { prefs ->
            prefs[PASSCODE_ATTEMPT_COUNT_NAME] = currentAttemptCount
        }

        return if (currentHash == calculateHash(passcode)) {
            resetBlock()
            PasscodeCheckStatus.Success
        } else {
            context.dataStore.edit { prefs ->
                if (currentAttemptCount == MAX_ATTEMPT_COUNT) {
                    prefs[PASSCODE_BLOCKED_UNTIL_NAME] = System.currentTimeMillis() + BLOCK_TIME
                }
            }
            PasscodeCheckStatus.NotMatch
        }
    }

    override fun hasPasscode(): Flow<Boolean> {
        return context.dataStore.data.map { it.passcodeHash != null }
    }

    override fun getRemainingAttempts() = context.dataStore.data.map {
        if (isBlockExpired()) resetBlock()
        max(MAX_ATTEMPT_COUNT - it.attemptCount, 0).toInt()
    }

    override fun getBlockEndTime() = context.dataStore.data.map {
        if (isBlockExpired()) resetBlock()
        it.blockedUntil
    }

    override suspend fun updatePasscode(passcode: String) {
        val (lastAuthorizedTimestamp, passcodeHash) = Passcode(passcodeHash = calculateHash(passcode))
        context.dataStore.edit { prefs ->
            prefs[PASSCODE_HASH_NAME] = passcodeHash
            prefs[PASSCODE_TIME_NAME] = lastAuthorizedTimestamp
        }
    }

    override suspend fun deletePasscode() {
        context.dataStore.edit {
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
        val preferences = context.dataStore.data.first()
        return System.currentTimeMillis() >= preferences.blockedUntil && MAX_ATTEMPT_COUNT == preferences.attemptCount
    }

    private suspend fun resetBlock() {
        context.dataStore.edit { prefs ->
            prefs[PASSCODE_BLOCKED_UNTIL_NAME] = 0
            prefs[PASSCODE_ATTEMPT_COUNT_NAME] = 0
        }
    }
}