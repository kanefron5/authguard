package dev.zabolotskikh.auth.domain

internal interface DataValidator<T> {
    fun check(input: T): Boolean
}