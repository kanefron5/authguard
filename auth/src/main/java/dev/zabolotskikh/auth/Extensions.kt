package dev.zabolotskikh.auth

import com.google.firebase.auth.FirebaseUser
import dev.zabolotskikh.authguard.domain.model.UserAccount

internal fun FirebaseUser.toUser(isOffline: Boolean = false) = UserAccount(
    displayName ?: email ?: "", email ?: "", photoUrl.toString(), isEmailVerified, uid, isOffline
)