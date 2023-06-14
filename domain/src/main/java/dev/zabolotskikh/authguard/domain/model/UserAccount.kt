package dev.zabolotskikh.authguard.domain.model

data class UserAccount(
    val name: String,
    val email: String,
    val photoUrl: String,
    val isEmailVerified: Boolean,
    val uid: String
)