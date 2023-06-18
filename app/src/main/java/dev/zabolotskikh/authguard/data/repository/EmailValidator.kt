package dev.zabolotskikh.authguard.data.repository

import android.util.Patterns.EMAIL_ADDRESS
import dev.zabolotskikh.authguard.domain.repository.DataValidator

class EmailValidator: DataValidator<String> {
    override fun check(input: String): Boolean {
        if (input.isEmpty()) return true
        return input.matches(EMAIL_ADDRESS.toRegex())
    }
}