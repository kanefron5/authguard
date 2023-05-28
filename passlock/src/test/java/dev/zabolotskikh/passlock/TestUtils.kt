@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zabolotskikh.passlock

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import dev.zabolotskikh.passlock.data.TestPasscodeEncoder
import dev.zabolotskikh.passlock.data.repository.PasscodeRepositoryImpl
import dev.zabolotskikh.passlock.domain.repository.PasscodeRepository
import dev.zabolotskikh.passlock.ui.activity.PasscodeViewModel
import dev.zabolotskikh.passlock.ui.provider.ProviderViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import java.io.File

private fun getDataStore(): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(produceFile = { File("test${System.currentTimeMillis()}.preferences_pb").apply { deleteOnExit() } })
}

internal fun getPasscodeRepository(): PasscodeRepository {
    return PasscodeRepositoryImpl(getDataStore(), TestPasscodeEncoder())
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
