@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zabolotskikh.passlock

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.work.Configuration
import androidx.work.WorkManager
import dev.zabolotskikh.passlock.data.TestPasscodeEncoder
import dev.zabolotskikh.passlock.data.repository.CurrentTimeRepositoryImpl
import dev.zabolotskikh.passlock.data.repository.PasscodeRepositoryImpl
import dev.zabolotskikh.passlock.domain.PasscodeEncoder
import dev.zabolotskikh.passlock.domain.repository.CurrentTimeRepository
import dev.zabolotskikh.passlock.domain.repository.PasscodeRepository
import dev.zabolotskikh.passlock.ui.activity.PasscodeViewModel
import dev.zabolotskikh.passlock.ui.provider.ProviderViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.io.File

private fun getDataStore(): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(produceFile = { File("test${System.currentTimeMillis()}.preferences_pb").apply { deleteOnExit() } })
}

private fun getWorker(): WorkManager = mock()

internal fun getPasscodeRepository(
    dataStore: DataStore<Preferences> = getDataStore(),
    passcodeEncoder: PasscodeEncoder = TestPasscodeEncoder(),
    currentTimeRepository: CurrentTimeRepository = CurrentTimeRepositoryImpl(),
    workManager: WorkManager = getWorker(),
): PasscodeRepository {
    return PasscodeRepositoryImpl(dataStore, passcodeEncoder, currentTimeRepository, workManager)
}

internal fun getPasscodeViewModel(
    passcodeRepository: PasscodeRepository = getPasscodeRepository(),
    dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
) = PasscodeViewModel(
    passcodeRepository, dispatcher
)

internal fun getProviderViewModel(
    passcodeRepository: PasscodeRepository = getPasscodeRepository(),
    dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
) = ProviderViewModel(
    passcodeRepository, dispatcher
)
