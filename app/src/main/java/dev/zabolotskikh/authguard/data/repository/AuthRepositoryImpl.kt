package dev.zabolotskikh.authguard.data.repository

import com.google.firebase.auth.FirebaseAuth
import dev.zabolotskikh.authguard.domain.repository.AuthRepository
import dev.zabolotskikh.authguard.toUser
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.joinAll
import javax.inject.Inject

// https://firebase.google.com/docs/auth/android/start?authuser=0&hl=en#kotlin+ktx_2
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    private val user = MutableStateFlow(firebaseAuth.currentUser)

    override suspend fun signUp(email: String, password: String) {
        val job: CompletableJob = Job()
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) job.complete()
            else job.completeExceptionally(it.exception ?: Exception("signUpWithEmail:failure"))
        }
        joinAll(job)
        user.emit(firebaseAuth.currentUser)
    }

    override suspend fun signIn(email: String, password: String) {
        val job: CompletableJob = Job()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) job.complete()
            else job.completeExceptionally(it.exception ?: Exception("signInWithEmail:failure"))
        }
        joinAll(job)
        user.emit(firebaseAuth.currentUser)
    }

    override fun isAuthenticated() = user.map { it != null }.distinctUntilChanged()

    override fun getUser() = user.map { it?.toUser() }.distinctUntilChanged()
}