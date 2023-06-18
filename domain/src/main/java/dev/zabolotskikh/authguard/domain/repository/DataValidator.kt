package dev.zabolotskikh.authguard.domain.repository

interface DataValidator<T> {
    fun check(input: T): Boolean
}