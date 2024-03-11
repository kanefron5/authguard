package dev.zabolotskikh.authguard.data

import dev.zabolotskikh.authguard.domain.model.UserAccount
import dev.zabolotskikh.authguard.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class TestAuthRepositoryImpl : AuthRepository {
    override suspend fun signUp(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() { }

    override suspend fun sendResetPasswordEmail(email: String) {
        TODO("Not yet implemented")
    }

    override suspend fun sendVerificationEmail() {
        TODO("Not yet implemented")
    }

    override fun isAuthenticated(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getUser(): Flow<UserAccount?> {
        TODO("Not yet implemented")
    }
}