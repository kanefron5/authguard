package dev.zabolotskikh.authguard.domain.repository

import dev.zabolotskikh.authguard.domain.model.UserAccount
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(email: String, password: String)
    suspend fun signIn(email: String, password: String)
    suspend fun signOut()
    suspend fun sendResetPasswordEmail(email: String)

    fun isAuthenticated(): Flow<Boolean>

    fun getUser(): Flow<UserAccount?>
}