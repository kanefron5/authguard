package dev.zabolotskikh.passlock.domain.repository

internal interface CurrentTimeRepository {
    fun now(): Long
}