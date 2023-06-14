@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zabolotskikh.passlock.ui.provider

import androidx.lifecycle.Lifecycle
import dev.zabolotskikh.passlock.MainDispatcherRule
import dev.zabolotskikh.passlock.domain.repository.PasscodeRepository
import dev.zabolotskikh.passlock.getProviderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class ProviderViewModelTests {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `test OnValidate event`() = runTest(mainDispatcherRule.dispatcher) {
        val viewModel = getProviderViewModel(dispatcher = mainDispatcherRule.dispatcher)
        Assert.assertNull(viewModel.state.value?.isLocked)
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(ProviderEvent.OnValidate)
        Assert.assertEquals(false, viewModel.state.value?.isLocked)
        job.cancel()
    }

    @Test
    fun `test ChangeLifecycle event with hasPasscode`() = runTest(mainDispatcherRule.dispatcher) {
        val repository = mock<PasscodeRepository> {
            onBlocking { hasPasscode() } doReturn flow { emit(true) }
        }

        val params = mapOf(
            Lifecycle.Event.ON_CREATE to true,
            Lifecycle.Event.ON_RESUME to false,
            Lifecycle.Event.ON_ANY to false,
            Lifecycle.Event.ON_DESTROY to false,
            Lifecycle.Event.ON_PAUSE to false,
            Lifecycle.Event.ON_START to false,
            Lifecycle.Event.ON_STOP to false,
        )
        for ((event, result) in params) {
            val viewModel = getProviderViewModel(
                dispatcher = mainDispatcherRule.dispatcher, passcodeRepository = repository
            )
            val job = launch { viewModel.state.collect() }
            viewModel.onEvent(ProviderEvent.ChangeLifecycle(event))
            Assert.assertEquals(result, viewModel.state.value?.isLocked)
            job.cancel()
        }
    }

    @Test
    fun `test ChangeLifecycle event with !hasPasscode`() = runTest(mainDispatcherRule.dispatcher) {
        val repository = mock<PasscodeRepository> {
            onBlocking { hasPasscode() } doReturn flow { emit(false) }
        }

        val params = mapOf(
            Lifecycle.Event.ON_CREATE to false,
            Lifecycle.Event.ON_RESUME to false,
            Lifecycle.Event.ON_ANY to false,
            Lifecycle.Event.ON_DESTROY to false,
            Lifecycle.Event.ON_PAUSE to false,
            Lifecycle.Event.ON_START to false,
            Lifecycle.Event.ON_STOP to false,
        )
        for ((event, result) in params) {
            val viewModel = getProviderViewModel(
                dispatcher = mainDispatcherRule.dispatcher, passcodeRepository = repository
            )
            val job = launch { viewModel.state.collect() }
            viewModel.onEvent(ProviderEvent.ChangeLifecycle(event))
            Assert.assertEquals(result, viewModel.state.value?.isLocked)
            job.cancel()
        }
    }

}