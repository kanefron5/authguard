package dev.zabolotskikh.authguard.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dev.zabolotskikh.authguard.domain.repository.AuthRepository
import dev.zabolotskikh.authguard.toUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// https://firebase.google.com/docs/auth/android/start?authuser=0&hl=en#kotlin+ktx_2
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    private val user = MutableStateFlow(firebaseAuth.currentUser)

    override suspend fun signUp(email: String, password: String) {
        val authResult: AuthResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        user.update { authResult.user }
    }

    override suspend fun signIn(email: String, password: String) {
        val authResult: AuthResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        user.update { authResult.user }
    }

    override suspend fun sendResetPasswordEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }

    override fun isAuthenticated() = user.map { it != null }.distinctUntilChanged()

    override fun getUser() = user.map { it?.toUser() }.distinctUntilChanged()
}