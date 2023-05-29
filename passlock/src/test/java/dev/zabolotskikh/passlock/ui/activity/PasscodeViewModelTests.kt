@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zabolotskikh.passlock.ui.activity

import dev.zabolotskikh.passlock.MainDispatcherRule
import dev.zabolotskikh.passlock.domain.model.PasscodeCheckStatus
import dev.zabolotskikh.passlock.domain.repository.PasscodeRepository
import dev.zabolotskikh.passlock.getPasscodeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class PasscodeViewModelTests {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val dispatcher = mainDispatcherRule.dispatcher

    @Test
    fun `test Cancel event`() = runTest(dispatcher) {
        val viewModel = getPasscodeViewModel(dispatcher = dispatcher)
        Assert.assertNull(viewModel.state.value.passcodeCheckStatus)
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(PasscodeEvent.Cancel)
        Assert.assertEquals(PasscodeResult.CANCELLED, viewModel.state.value.passcodeCheckStatus)
        job.cancel()
    }

    @Test
    fun `test SetPasscode event with blank arg`() = runTest(dispatcher) {
        val viewModel = getPasscodeViewModel(dispatcher = dispatcher)
        Assert.assertTrue(viewModel.state.value.passcode.isEmpty())
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(PasscodeEvent.SetPasscode(""))
        Assert.assertTrue(viewModel.state.value.passcode.isEmpty())
        job.cancel()
    }

    @Test
    fun `test SetPasscode event with not blank arg`() = runTest(dispatcher) {
        val viewModel = getPasscodeViewModel(dispatcher = dispatcher)
        Assert.assertTrue(viewModel.state.value.passcode.isEmpty())
        Assert.assertEquals(0, viewModel.state.value.attemptCount)
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(PasscodeEvent.SetPasscode("1234"))
        Assert.assertEquals("1234", viewModel.state.value.passcode)
        Assert.assertEquals(1, viewModel.state.value.attemptCount)
        job.cancel()
    }

    @Test
    fun `test SavePasscode event`() = runTest(dispatcher) {
        val repository = mock<PasscodeRepository>()
        val viewModel = getPasscodeViewModel(
            dispatcher = dispatcher, passcodeRepository = repository
        )
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(PasscodeEvent.SetPasscode("1234"))
        viewModel.onEvent(PasscodeEvent.SavePasscode)

        verify(repository).updatePasscode("1234")

        Assert.assertEquals(PasscodeResult.CONFIRMED, viewModel.state.value.passcodeCheckStatus)
        job.cancel()
    }

    @Test
    fun `test DeletePasscode event`() = runTest(dispatcher) {
        val repository = mock<PasscodeRepository>()
        val viewModel = getPasscodeViewModel(
            dispatcher = dispatcher, passcodeRepository = repository
        )
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(PasscodeEvent.DeletePasscode)

        verify(repository).deletePasscode()

        job.cancel()
    }

    @Test
    fun `test EnterPasscode event with BlockedUntil`() = runTest(dispatcher) {
        val repository = mock<PasscodeRepository> {
            onBlocking { checkPasscode(any()) } doReturn PasscodeCheckStatus.BlockedUntil(0)
        }
        val viewModel = getPasscodeViewModel(
            dispatcher = dispatcher, passcodeRepository = repository
        )
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(PasscodeEvent.EnterPasscode("1"))

        Assert.assertEquals(PasscodeResult.BLOCKED, viewModel.state.value.passcodeCheckStatus)
        job.cancel()
    }

    @Test
    fun `test EnterPasscode event with NoPasscode`() = runTest(dispatcher) {
        val repository = mock<PasscodeRepository> {
            onBlocking { checkPasscode(any()) } doReturn PasscodeCheckStatus.NoPasscode
        }
        val viewModel = getPasscodeViewModel(
            dispatcher = dispatcher, passcodeRepository = repository
        )
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(PasscodeEvent.EnterPasscode("1"))

        Assert.assertEquals(PasscodeResult.CANCELLED, viewModel.state.value.passcodeCheckStatus)
        job.cancel()
    }

    @Test
    fun `test EnterPasscode event with NotMatch`() = runTest(dispatcher) {
        val repository = mock<PasscodeRepository> {
            onBlocking { checkPasscode(any()) } doReturn PasscodeCheckStatus.NotMatch
        }
        val viewModel = getPasscodeViewModel(
            dispatcher = dispatcher, passcodeRepository = repository
        )
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(PasscodeEvent.EnterPasscode("1"))

        Assert.assertEquals(PasscodeResult.REJECTED, viewModel.state.value.passcodeCheckStatus)
        job.cancel()
    }

    @Test
    fun `test EnterPasscode event with Success`() = runTest(dispatcher) {
        val repository = mock<PasscodeRepository> {
            onBlocking { checkPasscode(any()) } doReturn PasscodeCheckStatus.Success
        }
        val viewModel = getPasscodeViewModel(
            dispatcher = dispatcher, passcodeRepository = repository
        )
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(PasscodeEvent.EnterPasscode("1"))

        Assert.assertEquals(PasscodeResult.SUCCEED, viewModel.state.value.passcodeCheckStatus)
        job.cancel()
    }

    @Test
    fun `test state in BLOCKED`() = runTest(dispatcher) {
        val repository = mock<PasscodeRepository> {
            onBlocking { getBlockEndTime() } doReturn flow { emit(System.currentTimeMillis() + 10000) }
        }
        val viewModel = getPasscodeViewModel(
            dispatcher = dispatcher, passcodeRepository = repository
        )
        val job = launch { viewModel.state.collect() }

        Assert.assertEquals(PasscodeResult.BLOCKED, viewModel.state.value.passcodeCheckStatus)
        job.cancel()
    }

    @Test
    fun `test state !in BLOCKED`() = runTest(dispatcher) {
        val repository = mock<PasscodeRepository> {
            onBlocking { getBlockEndTime() } doReturn flow { emit(System.currentTimeMillis() - 10000) }
        }
        val viewModel = getPasscodeViewModel(
            dispatcher = dispatcher, passcodeRepository = repository
        )
        val job = launch { viewModel.state.collect() }

        Assert.assertNull(viewModel.state.value.passcodeCheckStatus)
        job.cancel()
    }
}