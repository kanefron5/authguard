@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zabolotskikh.passlock.data

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.zabolotskikh.passlock.MainDispatcherRule
import dev.zabolotskikh.passlock.data.repository.CurrentTimeRepositoryImpl
import dev.zabolotskikh.passlock.domain.PasscodeEncoder
import dev.zabolotskikh.passlock.domain.model.PasscodeCheckStatus
import dev.zabolotskikh.passlock.getDataStore
import dev.zabolotskikh.passlock.getPasscodeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class PasscodeRepositoryImplTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `test updatePasscode`() = runTest(mainDispatcherRule.dispatcher) {
        val dataStore = getDataStore()
        val repository = getPasscodeRepository(
            dataStore = dataStore,
            passcodeEncoder = object : PasscodeEncoder {
                override fun encode(input: String) = input.toByteArray()
            }
        )

        Assert.assertFalse(repository.hasPasscode().first())
        repository.updatePasscode("1111")
        Assert.assertTrue(repository.hasPasscode().first())
        Assert.assertEquals("1111", dataStore.data.first()[stringPreferencesKey("passcode_hash")])
    }

    @Test
    fun `test deletePasscode`() = runTest(mainDispatcherRule.dispatcher) {
        val dataStore = getDataStore()
        val repository = getPasscodeRepository(
            dataStore = dataStore
        )
        repository.updatePasscode("1244")
        repeat(5) {
            repository.checkPasscode("1")
        }
        Assert.assertFalse(dataStore.data.first().asMap().isEmpty())
        repository.deletePasscode()
        Assert.assertTrue(dataStore.data.first().asMap().isEmpty())
    }

    @Test
    fun `test hasPasscode`() = runTest(mainDispatcherRule.dispatcher) {
        val dataStore = getDataStore()
        val repository = getPasscodeRepository(
            dataStore = dataStore
        )
        Assert.assertFalse(repository.hasPasscode().first())
        dataStore.edit {
            it[stringPreferencesKey("passcode_hash")] = "11111"
        }
        Assert.assertTrue(repository.hasPasscode().first())
    }

    @Test
    fun `test fail checkPasscode increment AttemptCount`() = runTest(mainDispatcherRule.dispatcher) {
        val dataStore = getDataStore()
        val repository = getPasscodeRepository(
            dataStore = dataStore
        )
        Assert.assertEquals(5, repository.getRemainingAttempts().first())

        dataStore.edit {
            it[stringPreferencesKey("passcode_hash")] = "11111"
        }
        repository.checkPasscode("22222")
        Assert.assertEquals(4, repository.getRemainingAttempts().first())
    }

    @Test
    fun `test fail checkPasscode returns NotMatch`() = runTest(mainDispatcherRule.dispatcher) {
        val dataStore = getDataStore()
        val repository = getPasscodeRepository(
            dataStore = dataStore
        )
        dataStore.edit {
            it[stringPreferencesKey("passcode_hash")] = "11111"
        }
        Assert.assertEquals(
            PasscodeCheckStatus.NotMatch,
            repository.checkPasscode("22222")
        )
    }

    @Test
    fun `test success checkPasscode returns Success`() = runTest(mainDispatcherRule.dispatcher) {
        val dataStore = getDataStore()
        val repository = getPasscodeRepository(
            dataStore = dataStore
        )
        dataStore.edit {
            it[stringPreferencesKey("passcode_hash")] = "11111"
        }
        Assert.assertEquals(
            PasscodeCheckStatus.Success,
            repository.checkPasscode("11111")
        )
    }

    @Test
    fun `test success checkPasscode clear NotMatch`() = runTest(mainDispatcherRule.dispatcher) {
        val dataStore = getDataStore()
        val repository = getPasscodeRepository(
            dataStore = dataStore
        )
        dataStore.edit {
            it[stringPreferencesKey("passcode_hash")] = "11111"
            it[longPreferencesKey("passcode_attempt_count")] = 4
        }

        repository.checkPasscode("11111")

        Assert.assertEquals(5, repository.getRemainingAttempts().first())
    }

    @Test
    fun `test 5 fail checkPasscode setting block`() = runTest(mainDispatcherRule.dispatcher) {
        val dataStore = getDataStore()
        val repository = getPasscodeRepository(
            dataStore = dataStore
        )
        dataStore.edit {
            it[stringPreferencesKey("passcode_hash")] = "11111"
        }

        repeat(4){
            Assert.assertEquals(
                PasscodeCheckStatus.NotMatch,
                repository.checkPasscode("1")
            )
        }

        val checkStatus = repository.checkPasscode("1")

        Assert.assertTrue(checkStatus is PasscodeCheckStatus.BlockedUntil)

        Assert.assertEquals(0, repository.getRemainingAttempts().first())
        Assert.assertNotNull(repository.getBlockEndTime().first())
        Assert.assertNotEquals(0, repository.getBlockEndTime().first())
    }

    @Test
    fun `test checkPasscode returns BlockedUntil if has active block`() = runTest(mainDispatcherRule.dispatcher) {
        val dataStore = getDataStore()
        val repository = getPasscodeRepository(
            dataStore = dataStore
        )
        val l = CurrentTimeRepositoryImpl().now() + 10_000
        dataStore.edit {
            it[stringPreferencesKey("passcode_hash")] = "11111"
            it[longPreferencesKey("passcode_blocked_until")] = l
            it[longPreferencesKey("passcode_attempt_count")] = 5
        }

        Assert.assertEquals(
            PasscodeCheckStatus.BlockedUntil(l),
            repository.checkPasscode("11111")
        )
    }

    @Test
    fun `test checkPasscode drops expired block`() = runTest(mainDispatcherRule.dispatcher) {
        val dataStore = getDataStore()
        val repository = getPasscodeRepository(
            dataStore = dataStore
        )
        val l = CurrentTimeRepositoryImpl().now() - 10_000
        dataStore.edit {
            it[stringPreferencesKey("passcode_hash")] = "11111"
            it[longPreferencesKey("passcode_blocked_until")] = l
            it[longPreferencesKey("passcode_attempt_count")] = 5
        }

        Assert.assertEquals(
            PasscodeCheckStatus.Success,
            repository.checkPasscode("11111")
        )

        Assert.assertEquals(5, repository.getRemainingAttempts().first())
        Assert.assertEquals(0, repository.getBlockEndTime().first())
    }

    @Test
    fun `test checkPasscode drops expired block with NotMatch`() = runTest(mainDispatcherRule.dispatcher) {
        val dataStore = getDataStore()
        val repository = getPasscodeRepository(
            dataStore = dataStore
        )
        val l = CurrentTimeRepositoryImpl().now() - 10_000
        dataStore.edit {
            it[stringPreferencesKey("passcode_hash")] = "11111"
            it[longPreferencesKey("passcode_blocked_until")] = l
            it[longPreferencesKey("passcode_attempt_count")] = 5
        }

        Assert.assertEquals(
            PasscodeCheckStatus.NotMatch,
            repository.checkPasscode("22222")
        )

        Assert.assertEquals(4, repository.getRemainingAttempts().first())
        Assert.assertEquals(0, repository.getBlockEndTime().first())
    }
}