package dev.zabolotskikh.passlock.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.zabolotskikh.passlock.domain.model.Passcode
import dev.zabolotskikh.passlock.domain.repository.PasscodeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val STORE_NAME = "passcode"
private val PASSCODE_HASH_NAME = stringPreferencesKey("passcode_hash")
private val PASSCODE_TIME_NAME = longPreferencesKey("passcode_last_entered")
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = STORE_NAME)

internal class PasscodeRepositoryImpl @Inject constructor(
    private val context: Context
) : PasscodeRepository {
    override suspend fun checkPasscode(hash: String): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[PASSCODE_HASH_NAME] == hash
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
        return "---$passcode$passcode---"
    }
}