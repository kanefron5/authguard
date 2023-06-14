package dev.zabolotskikh.passlock.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
internal class ResetBlockWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val dataStore: DataStore<Preferences>,
) : CoroutineWorker(context, workerParams) {

    private val keys: Array<String>? = workerParams.inputData.getStringArray(PARAM_RESET_BLOCK_KEYS)

    override suspend fun doWork(): Result {
        if (keys == null) return Result.failure()

        dataStore.edit { prefs ->
            keys.forEach { prefs.remove(stringPreferencesKey(it)) }
        }

        return Result.success()
    }

    companion object {
        const val PARAM_RESET_BLOCK_KEYS = "resetBlockKeys"
    }
}