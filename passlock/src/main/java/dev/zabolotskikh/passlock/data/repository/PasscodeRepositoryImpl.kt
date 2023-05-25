package dev.zabolotskikh.passlock.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.zabolotskikh.passlock.domain.model.Passcode
import dev.zabolotskikh.passlock.domain.model.PasscodeCheckStatus
import dev.zabolotskikh.passlock.domain.repository.PasscodeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val STORE_NAME = "passcode"
private const val BLOCK_TIME = 3 * 60 * 1000
private val PASSCODE_HASH_NAME = stringPreferencesKey("passcode_hash")
private val PASSCODE_TIME_NAME = longPreferencesKey("passcode_last_entered")
private val PASSCODE_ATTEMPT_COUNT_NAME = longPreferencesKey("passcode_attempt_count")
private val PASSCODE_BLOCKED_UNTIL_NAME = longPreferencesKey("passcode_blocked_until")

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = STORE_NAME)

internal class PasscodeRepositoryImpl @Inject constructor(
    private val context: Context
) : PasscodeRepository {
    override suspend fun checkPasscode(
        passcode: String, maxAttemptCount: Int
    ): PasscodeCheckStatus {
        val preferences = context.dataStore.data.first()
        println(preferences)
        val currentHash = preferences[PASSCODE_HASH_NAME]
        var currentAttemptCount = (preferences[PASSCODE_ATTEMPT_COUNT_NAME] ?: 0) + 1
        val currentBlockedUntil = preferences[PASSCODE_BLOCKED_UNTIL_NAME] ?: 0

        // todo разобраться с логикой блокировки
        if (currentHash == null) {
            return PasscodeCheckStatus.NoPasscode
        }
        if (System.currentTimeMillis() < currentBlockedUntil) {
            return PasscodeCheckStatus.BlockedUntil(currentBlockedUntil)
        } else {
            if ((currentAttemptCount - 1) == maxAttemptCount.toLong()) {
                currentAttemptCount = 1
                context.dataStore.edit { prefs ->
                    prefs[PASSCODE_BLOCKED_UNTIL_NAME] = 0
                }
            } else if (currentAttemptCount > maxAttemptCount) {
                val newBlockedUntil = System.currentTimeMillis() + BLOCK_TIME
                context.dataStore.edit { prefs ->
                    prefs[PASSCODE_BLOCKED_UNTIL_NAME] = newBlockedUntil
                }
                return PasscodeCheckStatus.BlockedUntil(newBlockedUntil)
            }
        }

        return if (currentHash == calculateHash(passcode)) {
            context.dataStore.edit { prefs ->
                prefs[PASSCODE_ATTEMPT_COUNT_NAME] = 0
            }
            PasscodeCheckStatus.Success
        } else {
            context.dataStore.edit { prefs ->
                prefs[PASSCODE_ATTEMPT_COUNT_NAME] = currentAttemptCount
            }
            PasscodeCheckStatus.NotMatch
        }
    }

    override fun hasPasscode(): Flow<Boolean> {
        return context.dataStore.data.map { it[PASSCODE_HASH_NAME] != null }
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
        }
    }


    private fun calculateHash(passcode: String): String {
        // TODO("Not yet implemented")
        return passcode.hashCode().toString()
    }
}