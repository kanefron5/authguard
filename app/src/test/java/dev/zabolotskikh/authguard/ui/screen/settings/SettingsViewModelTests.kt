@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zabolotskikh.authguard.ui.screen.settings

import dev.zabolotskikh.authguard.MainDispatcherRule
import dev.zabolotskikh.authguard.data.TestAppStateRepositoryImpl
import dev.zabolotskikh.authguard.ui.screen.getSettingsViewModelInstance
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTests {
    private val dispatcher = UnconfinedTestDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `test resetData`() = runTest(dispatcher) {
        val stateRepositoryImpl = TestAppStateRepositoryImpl()
        val settingsViewModel = getSettingsViewModelInstance(
            dispatcher = dispatcher,
            appStateRepository = stateRepositoryImpl
        )

        settingsViewModel.onEvent(SettingsEvent.ResetData)

        val appState = stateRepositoryImpl.state
        Assert.assertFalse(appState.isStarted)
        Assert.assertFalse(appState.isRemoteMode)
        Assert.assertFalse(appState.isPrivateMode)
    }
}