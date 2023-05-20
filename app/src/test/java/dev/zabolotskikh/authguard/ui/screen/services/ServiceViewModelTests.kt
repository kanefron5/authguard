@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zabolotskikh.authguard.ui.screen.services

import dev.zabolotskikh.authguard.MainDispatcherRule
import dev.zabolotskikh.authguard.data.TestAppStateRepositoryImpl
import dev.zabolotskikh.authguard.data.TestOtpRepositoryImpl
import dev.zabolotskikh.authguard.data.TestServiceRepositoryImpl
import dev.zabolotskikh.authguard.domain.model.GenerationMethod
import dev.zabolotskikh.authguard.domain.repository.AppStateRepository
import dev.zabolotskikh.authguard.domain.repository.OtpRepository
import dev.zabolotskikh.authguard.domain.repository.ServiceRepository
import dev.zabolotskikh.authguard.ui.screen.getServiceViewModelInstance
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class ServiceViewModelTests {
    private val dispatcher = UnconfinedTestDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `test SetName event`() = runTest(dispatcher) {
        val viewModel = getServiceViewModelInstance(dispatcher = dispatcher)
        Assert.assertEquals("", viewModel.state.value.name)
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(ServiceEvent.SetName("name"))
        Assert.assertEquals("name", viewModel.state.value.name)
        job.cancel()
    }

    @Test
    fun `test SetPrivateKey event with bad key`() = runTest(dispatcher) {
        val viewModel = getServiceViewModelInstance(dispatcher = dispatcher)
        Assert.assertEquals("", viewModel.state.value.privateKey)
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(ServiceEvent.SetPrivateKey("1"))
        Assert.assertEquals("1", viewModel.state.value.privateKey)
        Assert.assertTrue(viewModel.state.value.isBadSecret)
        job.cancel()
    }

    @Test
    fun `test SetPrivateKey event`() = runTest(dispatcher) {
        val viewModel = getServiceViewModelInstance(dispatcher = dispatcher)
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(ServiceEvent.SetPrivateKey("123"))
        Assert.assertEquals("123", viewModel.state.value.privateKey)
        Assert.assertFalse(viewModel.state.value.isBadSecret)
        job.cancel()
    }

    @Test
    fun `test SetMethod event`() = runTest(dispatcher) {
        val viewModel = getServiceViewModelInstance(dispatcher = dispatcher)
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(ServiceEvent.SetMethod(GenerationMethod.COUNTER))
        Assert.assertEquals(GenerationMethod.COUNTER, viewModel.state.value.method)
        job.cancel()
    }

    @Test
    fun `test ShowDialog event`() = runTest(dispatcher) {
        val viewModel = getServiceViewModelInstance(dispatcher = dispatcher)
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(ServiceEvent.ShowDialog)
        Assert.assertTrue(viewModel.state.value.isAddingService)
        job.cancel()
    }

    @Test
    fun `test HideDialog event`() = runTest(dispatcher) {
        val viewModel = getServiceViewModelInstance(dispatcher = dispatcher)
        val job = launch { viewModel.state.collect() }

        viewModel.onEvent(ServiceEvent.SetName("name"))
        viewModel.onEvent(ServiceEvent.SetPrivateKey("1"))
        viewModel.onEvent(ServiceEvent.ShowDialog)
        viewModel.onEvent(ServiceEvent.SetMethod(GenerationMethod.COUNTER))

        Assert.assertTrue(viewModel.state.value.isAddingService)
        Assert.assertTrue(viewModel.state.value.isBadSecret)
        Assert.assertEquals("1", viewModel.state.value.privateKey)
        Assert.assertEquals("name", viewModel.state.value.name)
        Assert.assertEquals(GenerationMethod.COUNTER, viewModel.state.value.method)

        viewModel.onEvent(ServiceEvent.HideDialog)

        Assert.assertFalse(viewModel.state.value.isAddingService)
        Assert.assertFalse(viewModel.state.value.isBadSecret)
        Assert.assertEquals("", viewModel.state.value.privateKey)
        Assert.assertEquals("", viewModel.state.value.name)
        Assert.assertEquals(GenerationMethod.TIME, viewModel.state.value.method)

        job.cancel()
    }

    @Test
    fun `test SaveService event`() = runTest(dispatcher) {
        val testServiceRepositoryImpl = TestServiceRepositoryImpl()
        val viewModel = getServiceViewModelInstance(
            dispatcher = dispatcher,
            serviceRepository = testServiceRepositoryImpl
        )
        val job = launch { viewModel.state.collect() }

        viewModel.onEvent(ServiceEvent.SetName("name"))
        viewModel.onEvent(ServiceEvent.SetPrivateKey("name"))
        viewModel.onEvent(ServiceEvent.ShowDialog)
        viewModel.onEvent(ServiceEvent.SetMethod(GenerationMethod.TIME))

        viewModel.onEvent(ServiceEvent.SaveService)

        Assert.assertFalse(viewModel.state.value.isAddingService)
        Assert.assertFalse(viewModel.state.value.isBadSecret)
        Assert.assertEquals("", viewModel.state.value.privateKey)
        Assert.assertEquals("", viewModel.state.value.name)
        Assert.assertEquals(GenerationMethod.TIME, viewModel.state.value.method)

        val list = viewModel.state.value.services

        Assert.assertEquals(1, list.count())

        val first = list.first()
        Assert.assertEquals("name", first.name)
        Assert.assertEquals("name", first.privateKey)
        Assert.assertEquals(GenerationMethod.TIME, first.generationMethod)

        job.cancel()
    }

    @Test
    fun `test DeleteService event`() = runTest(dispatcher) {
        val testServiceRepositoryImpl = TestServiceRepositoryImpl()
        val viewModel = getServiceViewModelInstance(
            dispatcher = dispatcher,
            serviceRepository = testServiceRepositoryImpl
        )
        val job = launch { viewModel.state.collect() }

        viewModel.onEvent(ServiceEvent.SetName("name"))
        viewModel.onEvent(ServiceEvent.SetPrivateKey("name"))
        viewModel.onEvent(ServiceEvent.ShowDialog)
        viewModel.onEvent(ServiceEvent.SetMethod(GenerationMethod.TIME))
        viewModel.onEvent(ServiceEvent.SaveService)

        val list = viewModel.state.value.services
        Assert.assertEquals(1, list.count())

        viewModel.onEvent(ServiceEvent.DeleteService(list.first()))

        Assert.assertEquals(0, viewModel.state.value.services.count())

        job.cancel()
    }

    @Test
    fun `test AddToFavorite event`() = runTest(dispatcher) {
        val testServiceRepositoryImpl = TestServiceRepositoryImpl()
        val viewModel = getServiceViewModelInstance(
            dispatcher = dispatcher,
            serviceRepository = testServiceRepositoryImpl
        )
        val job = launch {
            viewModel.state.collect()
        }

        viewModel.onEvent(ServiceEvent.SetName("name"))
        viewModel.onEvent(ServiceEvent.SetPrivateKey("name"))
        viewModel.onEvent(ServiceEvent.ShowDialog)
        viewModel.onEvent(ServiceEvent.SetMethod(GenerationMethod.TIME))
        viewModel.onEvent(ServiceEvent.SaveService)

        val list = viewModel.state.value.services
        Assert.assertEquals(1, list.count())

        viewModel.onEvent(ServiceEvent.AddToFavorite(list.first()))

        Assert.assertTrue(viewModel.state.value.services.first().isFavorite)

        job.cancel()
    }

    @Test
    fun `test RemoveFromFavorite event`() = runTest(dispatcher) {
        val testServiceRepositoryImpl = TestServiceRepositoryImpl()
        val viewModel = getServiceViewModelInstance(
            dispatcher = dispatcher,
            serviceRepository = testServiceRepositoryImpl
        )
        val job = launch {
            viewModel.state.collect()
        }

        viewModel.onEvent(ServiceEvent.SetName("name"))
        viewModel.onEvent(ServiceEvent.SetPrivateKey("name"))
        viewModel.onEvent(ServiceEvent.ShowDialog)
        viewModel.onEvent(ServiceEvent.SetMethod(GenerationMethod.TIME))
        viewModel.onEvent(ServiceEvent.SaveService)

        val list = viewModel.state.value.services
        Assert.assertEquals(1, list.count())

        viewModel.onEvent(ServiceEvent.AddToFavorite(list.first()))

        Assert.assertTrue(viewModel.state.value.services.first().isFavorite)

        viewModel.onEvent(ServiceEvent.RemoveFromFavorite(list.first()))

        Assert.assertFalse(viewModel.state.value.services.first().isFavorite)

        job.cancel()
    }

    @Test
    fun `test PrivateModeOn event`() = runTest(dispatcher) {
        val stateRepositoryImpl = TestAppStateRepositoryImpl()
        Assert.assertFalse(stateRepositoryImpl.state.isPrivateMode)

        val viewModel = getServiceViewModelInstance(
            dispatcher = dispatcher,
            appStateRepository = stateRepositoryImpl
        )
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(ServiceEvent.PrivateModeOn)
        Assert.assertTrue(viewModel.state.value.isPrivateMode)
        job.cancel()
    }

    @Test
    fun `test PrivateModeOff event`() = runTest(dispatcher) {
        val stateRepositoryImpl = TestAppStateRepositoryImpl()
        stateRepositoryImpl.state = stateRepositoryImpl.state.copy(isPrivateMode = true)
        Assert.assertTrue(stateRepositoryImpl.state.isPrivateMode)

        val viewModel = getServiceViewModelInstance(
            dispatcher = dispatcher,
            appStateRepository = stateRepositoryImpl
        )
        val job = launch { viewModel.state.collect() }
        viewModel.onEvent(ServiceEvent.PrivateModeOff)
        Assert.assertFalse(viewModel.state.value.isPrivateMode)
        job.cancel()
    }
}