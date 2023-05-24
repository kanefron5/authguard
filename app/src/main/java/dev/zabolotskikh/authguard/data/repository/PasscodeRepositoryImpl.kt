package dev.zabolotskikh.authguard.data.repository

import dev.zabolotskikh.authguard.domain.model.Passcode
import dev.zabolotskikh.authguard.domain.repository.AppStateRepository
import dev.zabolotskikh.authguard.domain.repository.PasscodeRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class PasscodeRepositoryImpl @Inject constructor(
    private val appStateRepository: AppStateRepository
) : PasscodeRepository {
    override suspend fun checkPasscode(hash: String): Boolean {
        val state = appStateRepository.getState().last()
        return state.passcode?.passcodeHash == hash
    }

    override suspend fun updatePasscode(passcode: String) {
        val appState = appStateRepository.getState().first()
        appStateRepository.update(
            appState.copy(
                passcode = Passcode(passcodeHash = calculateHash(passcode))
            )
        )
    }

    private fun calculateHash(passcode: String): String {
        // TODO("Not yet implemented")
        return "---$passcode$passcode---"
    }
}