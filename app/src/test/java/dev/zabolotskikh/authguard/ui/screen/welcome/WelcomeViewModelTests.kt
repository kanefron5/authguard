@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zabolotskikh.authguard.ui.screen.welcome

import dev.zabolotskikh.authguard.MainDispatcherRule
import dev.zabolotskikh.authguard.data.TestAppStateRepositoryImpl
import dev.zabolotskikh.authguard.ui.screen.getWelcomeViewModelInstance
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class WelcomeViewModelTests {
    private val dispatcher = UnconfinedTestDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `test startLocal`() = runTest(dispatcher) {
        val localAppStateRepository = TestAppStateRepositoryImpl()
        val viewModel = getWelcomeViewModelInstance(
            appStateRepository = localAppStateRepository,
            dispatcher = dispatcher
        )

        viewModel.startLocal()

        val appState = localAppStateRepository.state
        Assert.assertTrue(appState.isStarted)
        Assert.assertFalse(appState.isAuthenticated)
        Assert.assertFalse(appState.isPrivateMode)
    }
}