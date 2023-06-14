package dev.zabolotskikh.passlock.data.repository

import dev.zabolotskikh.passlock.domain.repository.CurrentTimeRepository

internal class CurrentTimeRepositoryImpl : CurrentTimeRepository {
    override fun now() = System.currentTimeMillis()
}