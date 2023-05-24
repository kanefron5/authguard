package dev.zabolotskikh.authguard.data.repository

import dev.zabolotskikh.authguard.data.local.dao.PasscodeDao
import dev.zabolotskikh.authguard.domain.model.Passcode
import dev.zabolotskikh.authguard.domain.repository.PasscodeRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PasscodeRepositoryImpl @Inject constructor(
    private val passcodeDao: PasscodeDao
) : PasscodeRepository {
    override suspend fun checkPasscode(hash: String): Boolean {
        val passcode = passcodeDao.getPasscode().first()
        return passcode?.passcodeHash == hash
    }

    override suspend fun updatePasscode(passcode: String) {
        val (lastAuthorizedTimestamp, passcodeHash) = Passcode(passcodeHash = calculateHash(passcode))
        passcodeDao.updateState(passcodeHash, lastAuthorizedTimestamp)
    }

    override suspend fun deletePasscode() = passcodeDao.resetPasscode()


    private fun calculateHash(passcode: String): String {
        // TODO("Not yet implemented")
        return "---$passcode$passcode---"
    }
}