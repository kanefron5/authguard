package dev.zabolotskikh.authguard.data.repository

import dev.zabolotskikh.authguard.domain.repository.DataValidator

class PasswordValidator: DataValidator<String> {
    override fun check(input: String): Boolean {
        if (input.isEmpty()) return true
        return input.matches(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[.,|`~\\[\\]<>{}!@#$%^&*+=()_\\-/\\\\\'\":;?]).{8,}$"))
    }
}